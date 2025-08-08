package uk.gov.justice.digital.hmpps.esurveillanceapi.service

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.esurveillanceapi.entity.Notification
import uk.gov.justice.digital.hmpps.esurveillanceapi.repository.NotificationRepository
import java.time.LocalDateTime

@Service
class NotificationService(
  private val notificationRepository: NotificationRepository,
) {
  fun saveNotification(
    personId: String,
    violation: String,
    message: String,
    timestamp: LocalDateTime = LocalDateTime.now(),
  ): Notification {
    val notification = Notification(
      personId = personId,
      violation = violation,
      timestamp = timestamp,
      message = message,
    )
    return notificationRepository.save(notification)
  }
}
