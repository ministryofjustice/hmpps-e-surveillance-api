package uk.gov.justice.digital.hmpps.esurveillanceapi.service

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import org.springframework.stereotype.Service
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import uk.gov.justice.digital.hmpps.esurveillanceapi.data.EventPayload
import uk.gov.justice.digital.hmpps.esurveillanceapi.data.SnsPayload
import uk.gov.justice.digital.hmpps.esurveillanceapi.entity.EventData
import uk.gov.justice.digital.hmpps.esurveillanceapi.resource.IngestResource.Companion.LOG

@Service
class EventsProcessorService(private val s3ClientBuilderService: S3ClientBuilderService) {

  fun processPersonId(outerJson: JsonNode) {
    val mapper = jacksonObjectMapper()
    val json = Json { ignoreUnknownKeys = true }
    val jsonString = mapper.writeValueAsString(outerJson)
    val eventSnsPayload = json.decodeFromString<SnsPayload>(jsonString)
    val eventPayload = eventSnsPayload.Message
    val event = json.decodeFromString<EventPayload>(eventPayload)
    val personId = event.personId
    val bucket = event.bucket
    val fileName = event.source

    val s3Client = s3ClientBuilderService.buildS3Client()
    val request = GetObjectRequest.builder()
      .bucket(bucket)
      .key(fileName)
      .build()
    s3Client.getObject(request).use { response ->
      LOG.info("File received from events CSV file: $fileName")
      val data = response.bufferedReader().readText()
      val eventData = csvReader().readAllWithHeader(data)
        .filter { row ->
          row["person_id"] == personId &&
            row["event_name"]?.removeSurrounding("'") != "EV_PARTIAL_CALLBACK"
        }
        .map { row ->
          EventData(
            personId = row["person_id"]?.removeSurrounding("'"),
            eventName = row["event_name"]?.removeSurrounding("'"),
            timestamp = row["timestamp"]?.removeSurrounding("'"),
          )
        }
      val jsonString = Json.encodeToString(ListSerializer(EventData.serializer()), eventData)

      LOG.info("Events received for $personId $jsonString")
    }
  }
}
