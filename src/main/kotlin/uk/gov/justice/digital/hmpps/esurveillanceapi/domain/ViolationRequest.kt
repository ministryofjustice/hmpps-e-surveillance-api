package uk.gov.justice.digital.hmpps.esurveillanceapi.domain


data class ViolationRequest (
  val events: List<Event>,
  val tone: String
)
