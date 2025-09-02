package uk.gov.justice.digital.hmpps.esurveillanceapi.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.awspring.cloud.sqs.annotation.SqsListener
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RequestBody

@Service
class MessageListener(
  private val fileProcessorService: FileProcessorService,
  private val eventsProcessorService: EventsProcessorService,
) {

  @SqsListener("\${hmpps.sqs.queues.fileuploadqueue.queueName}", factory = "hmppsQueueContainerFactoryProxy")
  fun processFileUploadMessage(@RequestBody rawMessage: String) {
    val outerJson = jacksonObjectMapper().readTree(rawMessage)
    fileProcessorService.processUploadedFile(outerJson)
  }

  @SqsListener("\${hmpps.sqs.queues.personidqueue.queueName}", factory = "hmppsQueueContainerFactoryProxy")
  fun processPersonIfdMessage(@RequestBody rawMessage: String) {
    val outerJson = jacksonObjectMapper().readTree(rawMessage)
    eventsProcessorService.processPersonId(outerJson)
  }
}
