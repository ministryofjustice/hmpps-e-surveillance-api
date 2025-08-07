package uk.gov.justice.digital.hmpps.esurveillanceapi.data

enum class ViolationType(
  val initiators: Set<String>,
  val terminators: Set<String>,
) {
  TAMPER(
    initiators = setOf("EV_PID_STRAP_TAMPER_START"),
    terminators = setOf("EV_PID_STRAP_TAMPER_END"),
  ),
  CURFEW_VIOLATION(
    initiators = setOf(
      "EV_ZONE_INCLUSION_TU_ABSENT_AT_START_TIME",
      "EV_CURFEWED_PID_ABSENT",
      "EV_PID_ABSENT",
      "EV_PID_ABSENT_DURING",
    ),
    terminators = setOf(
      "EV_ZONE_INCLUSION_TU_ARRIVED_DURING_TIME",
      "EV_CURFEWED_PID_ARRIVED",
      "EV_PID_ARRIVED",
    ),
  ),
  EXCLUSION_ZONE_VIOLATION(
    initiators = setOf(
      "EV_EXCLUDED_PID_ARRIVED_DURING_EXCLUSION",
      "EV_ZONE_EXCLUSION_TU_PRESENT_AT_START_TIME",
      "EV_ZONE_EXCLUSION_TU_ARRIVED_DURING_TIME",
    ),
    terminators = setOf(
      "EV_EXCLUDED_PID_DEPARTED_DURING_EXCLUSION",
      "EV_ZONE_EXCLUSION_TU_DEPARTED_DURING_TIME",
    ),
  ),
  ;

  companion object {
    fun fromInitiator(eventName: String): ViolationType? = entries.firstOrNull { eventName in it.initiators }

    fun fromTerminator(eventName: String): ViolationType? = entries.firstOrNull { eventName in it.terminators }
  }
}
