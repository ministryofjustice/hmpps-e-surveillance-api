package uk.gov.justice.digital.hmpps.esurveillanceapi.service

import uk.gov.justice.digital.hmpps.esurveillanceapi.data.NotificationTemplateIds
import uk.gov.justice.digital.hmpps.esurveillanceapi.data.Violation

object NotificationTemplateService {

  private val violationTemplateMap: Map<Violation, NotificationTemplateIds> = mapOf(
    Violation.TAMPERING_WITH_DEVICE to NotificationTemplateIds(
      smsId = "cf6c6c86-4510-4f77-bf70-af4e9979f085",
      emailId = "3ce9cad8-30f4-46b2-a810-00018623025c",
    ),
    Violation.ENTERING_EXCLUSION_ZONE to NotificationTemplateIds(
      smsId = "793c8c27-c6f5-47f2-b043-b30043920797",
      emailId = "8938a620-ad4a-4fdc-a3a0-1a63cad87179",
    ),
    Violation.MISSING_CURFEW to NotificationTemplateIds(
      smsId = "0fd38b72-009e-4cda-b83e-89de6fc372a5",
      emailId = "7f49bf4d-e3ff-4bfd-b0e9-105ae90a381a",
    ),
    Violation.BATTERY_LOW to NotificationTemplateIds(
      smsId = "22be7ba1-4d7e-4cfa-a556-95573bae0c47",
      emailId = "85bf318d-04c0-4d9b-a516-9683266ceeea",
    ),
  )

  fun getTemplateIds(violation: Violation): NotificationTemplateIds = violationTemplateMap[violation]
    ?: throw IllegalArgumentException("No template IDs configured for violation: $violation")
}
