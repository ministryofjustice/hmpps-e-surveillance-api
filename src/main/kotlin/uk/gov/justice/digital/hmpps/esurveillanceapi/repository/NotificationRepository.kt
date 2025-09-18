package uk.gov.justice.digital.hmpps.esurveillanceapi.repository

import org.springframework.data.jpa.repository.JpaRepository
import uk.gov.justice.digital.hmpps.esurveillanceapi.entity.Notification

interface NotificationRepository :
  JpaRepository<Notification, Long>, NotificationRepositoryCustom

