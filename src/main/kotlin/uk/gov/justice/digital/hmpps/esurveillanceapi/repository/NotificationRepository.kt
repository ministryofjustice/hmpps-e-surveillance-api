package uk.gov.justice.digital.hmpps.esurveillanceapi.repository

import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import uk.gov.justice.digital.hmpps.esurveillanceapi.entity.Notification

interface NotificationRepository :
  JpaRepository<Notification, Long>,
  JpaSpecificationExecutor<Notification> {

  fun searchNotifications(search: String): Specification<Notification> = Specification { root, _, cb ->
    cb.like(cb.lower(root.get("personName")), "%${search.lowercase()}%")
  }
}