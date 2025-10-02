package uk.gov.justice.digital.hmpps.esurveillanceapi.service

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.esurveillanceapi.config.NotifyTemplateProperties
import uk.gov.justice.digital.hmpps.esurveillanceapi.data.NotificationTemplateIds
import uk.gov.justice.digital.hmpps.esurveillanceapi.data.Violation

@Service
class NotificationTemplateService(
  private val notifyTemplateProperties: NotifyTemplateProperties,
) {

  fun getTemplateIds(violation: Violation): NotificationTemplateIds {
    val templateIds = when (violation) {
      Violation.TAMPERING_WITH_DEVICE -> notifyTemplateProperties.tamperingWithDevice
      Violation.ENTERING_EXCLUSION_ZONE -> notifyTemplateProperties.enteringExclusionZone
      Violation.MISSING_CURFEW -> notifyTemplateProperties.missingCurfew
      Violation.BATTERY_LOW -> notifyTemplateProperties.batteryLow
    }

    return NotificationTemplateIds(
      smsId = templateIds.smsId,
      emailId = templateIds.emailId,
    )
  }
}
