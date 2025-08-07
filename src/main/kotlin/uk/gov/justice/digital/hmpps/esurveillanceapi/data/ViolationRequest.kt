package uk.gov.justice.digital.hmpps.esurveillanceapi.data

import uk.gov.justice.digital.hmpps.esurveillanceapi.entity.Event

data class ViolationRequest(
  val events: List<Event>,
  val tone: String,
)
