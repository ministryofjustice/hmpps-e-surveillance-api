package uk.gov.justice.digital.hmpps.esurveillanceapi.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.awspring.cloud.sqs.annotation.SqsListener
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import jakarta.annotation.PostConstruct

@Service
class MessageListener(
  private val fileProcessorService: FileProcessorService,
  private val eventsProcessorService: EventsProcessorService,

  @Value("\${hmpps.sqs.queues.fileuploadqueue.queueName}")
  private val fileUploadQueueName: String,

  @Value("\${hmpps.sqs.queues.personidqueue.queueName}")
  private val personIdQueueName: String
) {

  @PostConstruct
  fun logQueues() {
    println("===== SQS Queues Configuration =====")
    println("FileUploadQueueName: $fileUploadQueueName")
    println("PersonIdQueueName: $personIdQueueName")
    println("===================================")
  }

  @SqsListener("#{target.fileUploadQueueName}", factory = "hmppsQueueContainerFactoryProxy")
  fun processFileUploadMessage(rawMessage: String) {
    val outerJson = jacksonObjectMapper().readTree(rawMessage)
    fileProcessorService.processUploadedFile(outerJson)
  }

  @SqsListener("#{target.personIdQueueName}", factory = "hmppsQueueContainerFactoryProxy")
  fun processPersonIdMessage(rawMessage: String) {
    val outerJson = jacksonObjectMapper().readTree(rawMessage)
    eventsProcessorService.processPersonId(outerJson)
  }
}
