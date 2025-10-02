package uk.gov.justice.digital.hmpps.esurveillanceapi.messages

import uk.gov.justice.digital.hmpps.esurveillanceapi.data.Violation
import uk.gov.justice.digital.hmpps.esurveillanceapi.service.MessageTemplate

val messageTemplates: Map<Violation, MessageTemplate> = mapOf(
  Violation.ENTERING_EXCLUSION_ZONE to enteringExclusionZoneTemplate,
  Violation.MISSING_CURFEW to missingCurfewTemplate,
  Violation.TAMPERING_WITH_DEVICE to tamperingWithDeviceTemplate,
)
