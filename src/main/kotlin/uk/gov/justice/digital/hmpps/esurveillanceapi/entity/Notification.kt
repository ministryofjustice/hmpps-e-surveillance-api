package uk.gov.justice.digital.hmpps.esurveillanceapi.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "notifications")
data class Notification(
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long = 0,

  val personId: String,

  val violation: String,

  val timestamp: LocalDateTime,

  @Column(columnDefinition = "TEXT")
  val message: String,
) {
  constructor() : this(0L, "", "", LocalDateTime.MIN, "")
}
