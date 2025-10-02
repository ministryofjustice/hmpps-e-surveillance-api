package uk.gov.justice.digital.hmpps.esurveillanceapi.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import uk.gov.service.notify.NotificationClient
import uk.gov.service.notify.NotificationClientException
import java.util.UUID

@Service
class NotifyService(
  private val notificationClient: NotificationClient,
) {
  companion object {
    val LOG: Logger = LoggerFactory.getLogger(this::class.java)
  }
  fun sendEmail(templateId: String, emailAddress: String, personalisation: Map<String, String>): UUID? {
    try {
      val response = notificationClient.sendEmail(
        templateId,
        emailAddress,
        personalisation,
        "eventAuditId",
      )
      return response.notificationId
    } catch (ex: NotificationClientException) {
      LOG.error("Error sending email with exception: $ex")
      throw ex
    }
  }

  fun sendSms(templateId: String, phoneNumber: String, personalisation: Map<String, String>): UUID? {
    try {
      val response = notificationClient.sendSms(
        templateId,
        phoneNumber,
        personalisation,
        "eventAuditId",
      )
      return response.notificationId
    } catch (ex: NotificationClientException) {
      LOG.error("Error sending sms with exception: $ex")
      throw ex
    }
  }
}
