package uk.gov.justice.digital.hmpps.esurveillanceapi.integration.service

import org.junit.jupiter.api.Test
import org.mockito.Mockito.anyString
import org.mockito.Mockito.eq
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles
import uk.gov.justice.digital.hmpps.esurveillanceapi.service.EventsProcessorService
import uk.gov.justice.hmpps.sqs.HmppsQueue
import uk.gov.justice.hmpps.sqs.HmppsQueueService
import uk.gov.service.notify.NotificationClient
import uk.gov.service.notify.SendSmsResponse

@SpringBootTest(properties = ["hmpps.sqs.enabled=false"])
@ActiveProfiles("test")
class EventProcessorServiceIntegrationTest {

  @MockBean
  lateinit var hmppsQueueService: HmppsQueueService

  @MockBean(name = "fileuploadqueue")
  lateinit var fileUploadQueue: HmppsQueue

  @MockBean(name = "fileuploadqueue-dlq")
  lateinit var fileUploadDlq: HmppsQueue

  @MockBean(name = "ingestionNotifyClient")
  lateinit var ingestionNotifyClient: NotificationClient

  @Autowired
  lateinit var eventsProcessorService: EventsProcessorService

  @Test
  fun `should use ingestion notify client to send SMS during event processing`() {
    val templateId = "test-template"
    val phone = "070000000000"
    val personalisation = mapOf("givenName" to "Alex")

    val mockResponse = mock(SendSmsResponse::class.java)
    `when`(
      ingestionNotifyClient.sendSms(eq(templateId), eq(phone), eq(personalisation), anyString()),
    ).thenReturn(mockResponse)

    ingestionNotifyClient.sendSms(templateId, phone, personalisation, "eventAuditId")

    verify(ingestionNotifyClient).sendSms(eq(templateId), eq(phone), eq(personalisation), anyString())
  }
}
