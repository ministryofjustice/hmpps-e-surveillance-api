package uk.gov.justice.digital.hmpps.esurveillanceapi.service

import io.awspring.cloud.sqs.annotation.SqsListener
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RequestBody
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

@Service
class FileUploadMessageListener(
  private val fileProcessorService: FileProcessorService,
  private val eventsProcessorService: EventsProcessorService
) {

  @SqsListener("fileuploadqueue", factory = "hmppsQueueContainerFactoryProxy")
  fun processFileUploadMessage(@RequestBody rawMessage: String,) {
    val outerJson = jacksonObjectMapper().readTree(rawMessage)
    fileProcessorService.processUploadedFile(outerJson)
  }

  @SqsListener("personidqueue", factory = "hmppsQueueContainerFactoryProxy")
  fun processPersonIfdMessage(@RequestBody rawMessage: String,) {
    val outerJson = jacksonObjectMapper().readTree(rawMessage)
    eventsProcessorService.processPersonId(outerJson)
  }
}
