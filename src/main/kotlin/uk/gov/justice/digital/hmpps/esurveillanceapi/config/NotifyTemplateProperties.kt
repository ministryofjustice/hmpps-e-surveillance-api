package uk.gov.justice.digital.hmpps.esurveillanceapi.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import uk.gov.justice.digital.hmpps.esurveillanceapi.data.NotificationTemplateIds

@Component
@ConfigurationProperties(prefix = "notify.templates")
class NotifyTemplateProperties {
  lateinit var tamperingWithDevice: NotificationTemplateIds
  lateinit var enteringExclusionZone: NotificationTemplateIds
  lateinit var missingCurfew: NotificationTemplateIds
  lateinit var batteryLow: NotificationTemplateIds
}
