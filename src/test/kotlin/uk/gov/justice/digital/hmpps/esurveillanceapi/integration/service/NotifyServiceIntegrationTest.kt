package uk.gov.justice.digital.hmpps.esurveillanceapi.integration.service

import org.assertj.core.api.Assertions.assertThat
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
import uk.gov.justice.digital.hmpps.esurveillanceapi.service.NotifyService
import uk.gov.justice.hmpps.sqs.HmppsQueueService
import uk.gov.service.notify.NotificationClient
import uk.gov.service.notify.SendEmailResponse
import uk.gov.service.notify.SendSmsResponse
import java.util.UUID

@SpringBootTest
@ActiveProfiles("test")
class NotifyServiceIntegrationTest {

  @MockBean(name = "primaryNotifyClient")
  lateinit var primaryNotifyClient: NotificationClient

  @MockBean
  lateinit var hmppsQueueService: HmppsQueueService

  @Autowired
  lateinit var notifyService: NotifyService

  @Test
  fun `should use primary notify client to send email`() {
    val templateId = "template-id"
    val email = "test@example.com"
    val personalisation = mapOf("name" to "Test")

    val mockResponse = mock(SendEmailResponse::class.java)
    val expectedId = UUID.randomUUID()
    `when`(mockResponse.notificationId).thenReturn(expectedId)
    `when`(
      primaryNotifyClient.sendEmail(eq(templateId), eq(email), eq(personalisation), anyString()),
    ).thenReturn(mockResponse)

    val response = notifyService.sendEmail(templateId, email, personalisation)

    verify(primaryNotifyClient).sendEmail(eq(templateId), eq(email), eq(personalisation), anyString())
    assertThat(response).isEqualTo(expectedId)
  }

  @Test
  fun `should use primary notify client to send SMS`() {
    val templateId = "template-id"
    val phone = "07000000000"
    val personalisation = mapOf("name" to "Test")

    val mockResponse = mock(SendSmsResponse::class.java)
    `when`(
      primaryNotifyClient.sendSms(eq(templateId), eq(phone), eq(personalisation), anyString()),
    ).thenReturn(mockResponse)

    val response = notifyService.sendSms(templateId, phone, personalisation)

    verify(primaryNotifyClient).sendSms(eq(templateId), eq(phone), eq(personalisation), anyString())
    assertThat(response).isEqualTo(mockResponse)
  }
}
