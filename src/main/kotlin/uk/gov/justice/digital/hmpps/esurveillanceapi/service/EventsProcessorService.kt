package uk.gov.justice.digital.hmpps.esurveillanceapi.service

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import kotlinx.serialization.json.Json
import org.springframework.stereotype.Service
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import uk.gov.justice.digital.hmpps.esurveillanceapi.data.EventPayload
import uk.gov.justice.digital.hmpps.esurveillanceapi.data.SnsPayload
import uk.gov.justice.digital.hmpps.esurveillanceapi.entity.Event
import uk.gov.justice.digital.hmpps.esurveillanceapi.entity.Persons
import uk.gov.justice.digital.hmpps.esurveillanceapi.repository.EventRepository
import uk.gov.justice.digital.hmpps.esurveillanceapi.repository.PersonsRepository
import uk.gov.justice.digital.hmpps.esurveillanceapi.resource.IngestResource.Companion.LOG

@Service
class EventsProcessorService(
  private val s3ClientBuilderService: S3ClientBuilderService,
  private val eventRepository: EventRepository,
  private val personsRepository: PersonsRepository,
  private val violationDetector: ViolationDetector,
  private val notificationService: NotificationService,
) {

  fun processPersonId(outerJson: JsonNode) {
    val mapper = jacksonObjectMapper()
    val json = Json { ignoreUnknownKeys = true }
    val jsonString = mapper.writeValueAsString(outerJson)
    val eventSnsPayload = json.decodeFromString<SnsPayload>(jsonString)
    val eventPayload = eventSnsPayload.Message
    val event = json.decodeFromString<EventPayload>(eventPayload)
    val personId = event.personId.removeSurrounding("'")
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
        .filter { row -> row["person_id"] == personId && row["event_name"]?.removeSurrounding("'") != "EV_PARTIAL_CALLBACK" }
        .mapNotNull { row ->
          val eventName = row["event_name"]?.removeSurrounding("'")
          val timestamp = row["timestamp"]?.removeSurrounding("'")
          if (eventName != null && timestamp != null) {
            Event(
              personId = personId,
              eventName = eventName,
              timestamp = timestamp,
            )
          } else {
            null
          }
        }
      LOG.info("Event Data $eventData  for personId: $personId")
      if (eventData.isNotEmpty()) {
        eventRepository.saveAll(eventData)
        LOG.info("Saved ${eventData.size} events for personId: $personId")
        val dbEvents = eventRepository.findByPersonIdOrderByTimestampAsc(personId)
        val user: Persons? = personsRepository.findByPersonId(personId)

        val violation = violationDetector.detectViolation(dbEvents)
        LOG.info("Violation:  $violation for $dbEvents by user $user ")
        if (violation != null) {
          val violationString = violationDetector.mapViolationTypeToViolation(violation)
          if (user != null) {
            val message = generateMessage(user.givenName, violationString, Tone.SUPPORTIVE)
            notificationService.saveNotification(personId, violationString.name, message)
          } else {
            LOG.info("No person found for personId: $personId")
          }
        } else {
          LOG.info("No violation found for: $personId\ndbEvents")
        }
      } else {
        LOG.info("No valid events found to save for personId: $personId")
      }
    }
  }
}
