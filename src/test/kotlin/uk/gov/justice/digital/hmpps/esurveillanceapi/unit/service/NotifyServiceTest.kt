package uk.gov.justice.digital.hmpps.esurveillanceapi.unit.service

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import uk.gov.justice.digital.hmpps.esurveillanceapi.service.NotifyService
import uk.gov.service.notify.NotificationClient
import uk.gov.service.notify.NotificationClientException
import uk.gov.service.notify.SendEmailResponse
import uk.gov.service.notify.SendSmsResponse
import java.util.UUID

class NotifyServiceTest {

  private val notificationClient = mock(NotificationClient::class.java)
  private val notifyService = NotifyService(notificationClient)

  @Test
  fun `sendEmail should return notificationId when email is sent successfully`() {
    val templateId = "template-id"
    val email = "test@example.com"
    val personalisation = mapOf("name" to "Test User")
    val mockResponse = mock(SendEmailResponse::class.java)
    val expectedNotificationId = UUID.randomUUID()

    `when`(mockResponse.notificationId).thenReturn(expectedNotificationId)
    `when`(notificationClient.sendEmail(templateId, email, personalisation, "eventAuditId"))
      .thenReturn(mockResponse)

    val result = notifyService.sendEmail(templateId, email, personalisation)

    assertEquals(expectedNotificationId, result)
    verify(notificationClient).sendEmail(templateId, email, personalisation, "eventAuditId")
  }

  @Test
  fun `sendEmail should throw NotificationClientException when sending fails`() {
    val templateId = "template-id"
    val email = "fail@example.com"
    val personalisation = mapOf("key" to "value")

    `when`(notificationClient.sendEmail(templateId, email, personalisation, "eventAuditId"))
      .thenThrow(NotificationClientException("Email failed"))

    assertThrows<NotificationClientException> {
      notifyService.sendEmail(templateId, email, personalisation)
    }

    verify(notificationClient).sendEmail(templateId, email, personalisation, "eventAuditId")
  }

  @Test
  fun `sendSms should return SendSmsResponse when sms is sent successfully`() {
    val templateId = "template-id"
    val phoneNumber = "07123456789"
    val personalisation = mapOf("code" to "123456")
    val expectedResponse = mock(SendSmsResponse::class.java)

    `when`(notificationClient.sendSms(templateId, phoneNumber, personalisation, "eventAuditId"))
      .thenReturn(expectedResponse)

    val result = notifyService.sendSms(templateId, phoneNumber, personalisation)

    assertEquals(expectedResponse, result)
    verify(notificationClient).sendSms(templateId, phoneNumber, personalisation, "eventAuditId")
  }

  @Test
  fun `sendSms should throw NotificationClientException when sending fails`() {
    val templateId = "template-id"
    val phoneNumber = "07000000000"
    val personalisation = mapOf("key" to "value")

    `when`(notificationClient.sendSms(templateId, phoneNumber, personalisation, "eventAuditId"))
      .thenThrow(NotificationClientException("SMS failed"))

    assertThrows<NotificationClientException> {
      notifyService.sendSms(templateId, phoneNumber, personalisation)
    }

    verify(notificationClient).sendSms(templateId, phoneNumber, personalisation, "eventAuditId")
  }
}
