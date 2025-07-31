package uk.gov.justice.digital.hmpps.esurveillanceapi.data

import kotlinx.serialization.*

@Serializable
data class SnsPayload(
  val Message: String
)

@Serializable
data class EventPayload(
  val personId: String,
  val source: String,
  val bucket: String
)