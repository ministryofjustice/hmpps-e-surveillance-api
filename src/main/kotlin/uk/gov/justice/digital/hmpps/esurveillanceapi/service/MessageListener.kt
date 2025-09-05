package uk.gov.justice.digital.hmpps.esurveillanceapi.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.awspring.cloud.sqs.annotation.SqsListener
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RequestBody

@Service
class MessageListener(
  private val fileProcessorService: FileProcessorService,
  private val eventsProcessorService: EventsProcessorService,
) {

  companion object {
    private val LOG: Logger = LoggerFactory.getLogger(MessageListener::class.java)
  }

  @SqsListener("fileuploadqueue", factory = "hmppsQueueContainerFactoryProxy")
  fun processFileUploadMessage(@RequestBody rawMessage: String) {
    val outerJson = jacksonObjectMapper().readTree(rawMessage)
    try {
      fileProcessorService.processUploadedFile(outerJson)
    } catch (e: Exception) {
      LOG.error("Failed to process message from fileuploadqueue: $outerJson", e)
      throw e
    }
  }

  @SqsListener("personidqueue", factory = "hmppsQueueContainerFactoryProxy")
  fun processPersonIfdMessage(@RequestBody rawMessage: String) {
    val outerJson = jacksonObjectMapper().readTree(rawMessage)
    try {
      eventsProcessorService.processPersonId(outerJson)
    } catch (e: Exception) {
      LOG.error("Failed to process message from personidqueue: $outerJson", e)
      throw e
    }
  }
}
