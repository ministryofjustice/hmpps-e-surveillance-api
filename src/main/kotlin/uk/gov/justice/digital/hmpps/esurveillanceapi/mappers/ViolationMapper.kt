package uk.gov.justice.digital.hmpps.esurveillanceapi.mappers

import uk.gov.justice.digital.hmpps.esurveillanceapi.domain.ViolationType
import uk.gov.justice.digital.hmpps.esurveillanceapi.service.Violation

fun mapViolationTypeToViolation(type: ViolationType): Violation = when (type) {
  ViolationType.EXCLUSION_ZONE_VIOLATION -> Violation.ENTERING_EXCLUSION_ZONE
  ViolationType.CURFEW_VIOLATION -> Violation.MISSING_CURFEW
  ViolationType.TAMPER -> Violation.TAMPERING_WITH_DEVICE
}
