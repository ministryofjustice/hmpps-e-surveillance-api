package uk.gov.justice.digital.hmpps.esurveillanceapi.entity

import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.serialization.Serializable

@Serializable
data class EventData(
  @JsonProperty("person_id") val personId: String?,
  @JsonProperty("event_name") val eventName: String?,
  @JsonProperty("timestamp") val timestamp: String?,
)
