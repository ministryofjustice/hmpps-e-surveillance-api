package uk.gov.justice.digital.hmpps.esurveillanceapi.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "events")
data class Event(
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long = 0,

  val personId: String,
  val eventName: String,
  val timestamp: String,
) {
  constructor() : this(0L, "", "", "")
}
