package uk.gov.justice.digital.hmpps.esurveillanceapi.service

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.esurveillanceapi.domain.Event
import uk.gov.justice.digital.hmpps.esurveillanceapi.domain.ViolationType

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
}
