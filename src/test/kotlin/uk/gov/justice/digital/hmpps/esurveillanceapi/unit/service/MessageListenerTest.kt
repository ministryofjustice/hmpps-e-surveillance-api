package uk.gov.justice.digital.hmpps.esurveillanceapi.unit.service

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import uk.gov.justice.digital.hmpps.esurveillanceapi.service.EventsProcessorService
import uk.gov.justice.digital.hmpps.esurveillanceapi.service.FileProcessorService
import uk.gov.justice.digital.hmpps.esurveillanceapi.service.MessageListener

class MessageListenerTest {

  private val fileProcessorService: FileProcessorService = mock()
  private val eventsProcessorService: EventsProcessorService = mock()
  private val listener = MessageListener(fileProcessorService, eventsProcessorService)

  private val objectMapper = jacksonObjectMapper()

  @Test
  fun `should process file upload message`() {
    val message = """{"key": "value"}"""

    listener.processFileUploadMessage(message)

    val expectedJson: JsonNode = objectMapper.readTree(message)
    verify(fileProcessorService).processUploadedFile(expectedJson)
  }

  @Test
  fun `should process person ID message`() {
    val message = """{"personId": "123"}"""

    listener.processPersonIfdMessage(message)

    val expectedJson: JsonNode = objectMapper.readTree(message)
    verify(eventsProcessorService).processPersonId(expectedJson)
  }

  @Test
  fun `should log and rethrow exception from fileProcessorService`() {
    val message = """{"key": "value"}"""
    val jsonNode = objectMapper.readTree(message)
    whenever(fileProcessorService.processUploadedFile(jsonNode)).thenThrow(RuntimeException("fail"))

    assertThrows<RuntimeException> {
      listener.processFileUploadMessage(message)
    }

    verify(fileProcessorService).processUploadedFile(jsonNode)
  }

  @Test
  fun `should log and rethrow exception from eventsProcessorService`() {
    val message = """{"personId": "123"}"""
    val jsonNode = objectMapper.readTree(message)
    whenever(eventsProcessorService.processPersonId(jsonNode)).thenThrow(RuntimeException("fail"))

    assertThrows<RuntimeException> {
      listener.processPersonIfdMessage(message)
    }

    verify(eventsProcessorService).processPersonId(jsonNode)
  }
}
