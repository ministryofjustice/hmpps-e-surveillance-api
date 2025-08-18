package uk.gov.justice.digital.hmpps.esurveillanceapi.service

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import software.amazon.awssdk.services.sns.SnsClient
import software.amazon.awssdk.services.sns.model.PublishRequest
import uk.gov.justice.digital.hmpps.esurveillanceapi.entity.Persons
import uk.gov.justice.digital.hmpps.esurveillanceapi.repository.PersonsRepository
import uk.gov.justice.digital.hmpps.esurveillanceapi.resource.IngestResource.Companion.LOG
import java.net.URI
import kotlin.text.removeSurrounding
import kotlin.text.toLong

@Service
class FileProcessorService(
  private val personsRepository: PersonsRepository,
  private val s3ClientBuilderService: S3ClientBuilderService,
) {
  @Value("\${aws.credentials.accessKey}")
  private lateinit var accessKey: String

  @Value("\${aws.credentials.secretKey}")
  private lateinit var secretKey: String

  @Value("\${aws.region}")
  private lateinit var region: String

  @Value("\${aws.sns.endpoint}")
  private lateinit var snsEndpoint: String

  @Value("\${aws.topic-arn.events}")
  private lateinit var eventsTopicArn: String

  private val objectMapper = jacksonObjectMapper()

  fun processPosUsers(bucket: String, key: String) {
    val s3Client = s3ClientBuilderService.buildS3Client()
    val request = GetObjectRequest.builder()
      .bucket(bucket)
      .key(key)
      .build()
    try {
      s3Client.getObject(request).use { response ->
        LOG.info("Data received from pop CSV file: $key")

        val data = response.bufferedReader().readText()
        val persons: List<Persons> = csvReader().readAllWithHeader(data).map { row ->
          Persons(
            id = row["id"]?.toLong() ?: 0,
            deliusId = row["delius_id"]?.removeSurrounding("'") ?: "",
            uniqueDeviceWearerId = row["unique_device_wearer_id"]?.removeSurrounding("'") ?: "",
            personId = row["person_id"]?.removeSurrounding("'") ?: "",
            givenName = row["given_name"]?.removeSurrounding("'") ?: "",
            familyName = row["family_name"]?.removeSurrounding("'") ?: "",
            alias = row["alias"]?.removeSurrounding("'") ?: "",
            createdAt = row["timestamp"]?.removeSurrounding("'") ?: "",
            toy = row["toy"]?.removeSurrounding("'").toBoolean(),
          )
        }

        personsRepository.saveAll(persons)
      }
    } catch (e: Exception) {
      LOG.error("Error while processing pop file: ${e.message}")
    }
  }

  fun processEvents(bucket: String, key: String) {
    val s3Client = s3ClientBuilderService.buildS3Client()
    val request = GetObjectRequest.builder()
      .bucket(bucket)
      .key(key)
      .build()
    val snsClient = buildSnsClient()
    s3Client.getObject(request).use { response ->
      LOG.info("Data received from events CSV file: $key")

      val data = response.bufferedReader().readText()

      val uniquePersonIds = csvReader().readAllWithHeader(data)
        .mapNotNull { it["person_id"] }
        .toSet()
      snsClient.use {
        for (id in uniquePersonIds) {
          val messagePayload = mapOf(
            "personId" to id,
            "source" to key,
            "bucket" to bucket,
          )
          val messageJson = Json.encodeToString(messagePayload)
          val request = PublishRequest.builder()
            .message(messageJson)
            .topicArn(eventsTopicArn)
            .build()

          val response = snsClient.publish(request)
          println("Message ID: ${response.messageId()} for message: $id")
        }
      }
    }
  }

  private fun processFile(bucket: String, key: String) {
    LOG.info("Data received from file $key for $bucket")
    if (key.contains("pop")) {
      processPosUsers(bucket, key)
    } else if (key.contains("event")) {
      processEvents(bucket, key)
    }
  }

  private fun buildSnsClient(): SnsClient = SnsClient.builder()
    .endpointOverride(URI.create(snsEndpoint))
    .region(Region.of(region))
    .build()

  fun processUploadedFile(outerJson: JsonNode) {
    val messageJson = outerJson["Message"]?.asText()

    messageJson?.let {
      val event = objectMapper.readTree(it)
      val bucket = event["Records"][0]["s3"]["bucket"]["name"].asText()
      val key = event["Records"][0]["s3"]["object"]["key"].asText()

      LOG.info("New S3 object created: $bucket/$key")
      processFile(bucket, key)
    }
  }
}
