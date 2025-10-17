package uk.gov.justice.digital.hmpps.esurveillanceapi.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import uk.gov.service.notify.NotificationClient

@Configuration
class NotifyConfiguration(
  @param:Value("\${notify.api.key:invalidKey}") private val apiKey: String,
  @param:Value("\${notify.api.ingestionKey:invalidKey}") private val ingestionApiKey: String,
) {
  @Bean(name = ["primaryNotifyClient"])
  fun notifyClient(): NotificationClient = NotificationClient(apiKey)

  @Bean(name = ["ingestionNotifyClient"])
  fun ingestionNotifyClient(): NotificationClient = NotificationClient(ingestionApiKey)
}
