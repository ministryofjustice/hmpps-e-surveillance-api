package uk.gov.justice.digital.hmpps.esurveillanceapi.service

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.esurveillanceapi.data.Event
import uk.gov.justice.digital.hmpps.esurveillanceapi.data.ViolationType

@Service
class ViolationDetector {
  fun detectViolation(events: List<Event>): ViolationType? {
    val activeViolations = mutableMapOf<ViolationType, Boolean>()

    for (event in events) {
      ViolationType.fromInitiator(event.eventType)?.let {
        activeViolations[it] = true
      }

      ViolationType.fromTerminator(event.eventType)?.let {
        if (activeViolations.getOrDefault(it, false)) {
          activeViolations[it] = false
        }
      }
    }

    return activeViolations.entries.firstOrNull { it.value }?.key
  }

  fun mapViolationTypeToViolation(type: ViolationType): Violation = when (type) {
    ViolationType.EXCLUSION_ZONE_VIOLATION -> Violation.ENTERING_EXCLUSION_ZONE
    ViolationType.CURFEW_VIOLATION -> Violation.MISSING_CURFEW
    ViolationType.TAMPER -> Violation.TAMPERING_WITH_DEVICE
  }
}
