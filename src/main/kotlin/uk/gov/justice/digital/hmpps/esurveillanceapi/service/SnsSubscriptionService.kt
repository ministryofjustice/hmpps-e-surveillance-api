package uk.gov.justice.digital.hmpps.esurveillanceapi.service

import com.fasterxml.jackson.databind.JsonNode
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.esurveillanceapi.resource.IngestResource.Companion.LOG
import java.net.HttpURLConnection
import java.net.URI

@Service
class SnsSubscriptionService {

  fun handleSubscriptionConfirmation(outerJson: JsonNode) {
    val url = outerJson["SubscribeURL"]?.asText()
    LOG.info("Confirming subscription at: $url")
    try {
      val connection = URI(url ?: error("subscribeUrl is null")).toURL().openConnection() as HttpURLConnection
      connection.requestMethod = "GET"
      val responseCode = connection.responseCode
      LOG.info("Subscription confirmed. Response code: $responseCode")
    } catch (e: Exception) {
      LOG.error("Failed to confirm subscription: ${e.message}")
    }
  }
}
