package uk.gov.justice.digital.hmpps.esurveillanceapi.messages

import uk.gov.justice.digital.hmpps.esurveillanceapi.service.MessageTemplate
import uk.gov.justice.digital.hmpps.esurveillanceapi.service.Violation

val messageTemplates: Map<Violation, MessageTemplate> = mapOf(
  Violation.ENTERING_EXCLUSION_ZONE to enteringExclusionZoneTemplate,
  Violation.MISSING_CURFEW to missingCurfewTemplate,
  Violation.TAMPERING_WITH_DEVICE to tamperingWithDeviceTemplate,
)
