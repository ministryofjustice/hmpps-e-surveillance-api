package uk.gov.justice.digital.hmpps.esurveillanceapi.data

data class Event(
  val personId: String,
  val eventType: String,
  val timestamp: String,
)
