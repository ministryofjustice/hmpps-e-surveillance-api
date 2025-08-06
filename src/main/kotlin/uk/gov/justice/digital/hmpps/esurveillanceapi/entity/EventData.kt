package uk.gov.justice.digital.hmpps.esurveillanceapi.entity

import kotlinx.serialization.Serializable

@Serializable
data class EventData(
  val personId: String?,
  val eventName: String?,
  val timestamp: String?,
)
