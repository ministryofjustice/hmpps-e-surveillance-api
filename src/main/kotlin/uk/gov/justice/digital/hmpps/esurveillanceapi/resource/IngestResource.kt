package uk.gov.justice.digital.hmpps.esurveillanceapi.resource

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import uk.gov.justice.digital.hmpps.esurveillanceapi.service.EventsProcessorService
import uk.gov.justice.digital.hmpps.esurveillanceapi.service.FileProcessorService
import uk.gov.justice.digital.hmpps.esurveillanceapi.service.SnsSubscriptionService
import kotlin.String

@RestController
@RequestMapping("/ingest")
class IngestResource(private val fileProcessorService: FileProcessorService,
                     private val snsSubscriptionService: SnsSubscriptionService,
                     private val eventsProcessorService: EventsProcessorService) {
  companion object {
    val LOG: Logger = LoggerFactory.getLogger(this::class.java)
  }
  private val objectMapper = jacksonObjectMapper()

  @PostMapping(
    value = ["/"],
    consumes = [MediaType.TEXT_PLAIN_VALUE],
    produces = [MediaType.TEXT_PLAIN_VALUE]
  )
  fun ingestUploadFile(
    @RequestBody rawMessage: String,
    @RequestHeader("x-amz-sns-message-type", required = false) messageType: String?
  ): ResponseEntity<String> {
    LOG.info("Received SNS MessageType: $messageType")

    val outerJson = objectMapper.readTree(rawMessage)

    when (messageType) {
      "SubscriptionConfirmation" -> snsSubscriptionService.handleSubscriptionConfirmation(outerJson)
      "Notification" -> fileProcessorService.processUploadedFile(outerJson)
      else -> LOG.warn("Unknown message type: $messageType")
    }

    return ResponseEntity.ok("Processed")
  }

  @PostMapping(
    value = ["/events"],
    consumes = [MediaType.TEXT_PLAIN_VALUE],
    produces = [MediaType.TEXT_PLAIN_VALUE]
  )
  fun ingestEvents(
    @RequestBody rawMessage: String,
    @RequestHeader("x-amz-sns-message-type", required = false) messageType: String?
  ): ResponseEntity<String> {
    LOG.info("Received SNS MessageType: $messageType")

    val outerJson = objectMapper.readTree(rawMessage)
    when (messageType) {
      "SubscriptionConfirmation" -> snsSubscriptionService.handleSubscriptionConfirmation(outerJson)
      "Notification" -> eventsProcessorService.processPersonId(outerJson)
      else -> LOG.warn("Unknown message type: $messageType")
    }

    return ResponseEntity.ok("Processed")
  }

}
