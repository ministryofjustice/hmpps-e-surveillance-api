package uk.gov.justice.digital.hmpps.esurveillanceapi.resource

import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.sns.SnsClient
import software.amazon.awssdk.services.sns.model.SubscribeRequest
import uk.gov.justice.digital.hmpps.esurveillanceapi.resource.IngestResource.Companion.LOG
import java.net.URI

@RestController
@RequestMapping("/test")
class SubscriptionResource {

  @GetMapping("/hello")
  fun sayHello(@RequestParam(name = "name", defaultValue = "World") name: String): String = "Hello, $name!"

  @Value("\${aws.sns.endpoint}")
  private lateinit var snsEndpoint: String

  @Value("\${aws.region}")
  private lateinit var region: String

  @Value("\${aws.credentials.accessKey}")
  private lateinit var accessKey: String

  @Value("\${aws.credentials.secretKey}")
  private lateinit var secretKey: String

  @Value("\${aws.topic-arn.file-upload}")
  private lateinit var fileUploadTopicArn: String

  @Value("\${aws.topic-arn.events}")
  private lateinit var eventsTopicArn: String

  @Value("\${end-point.ingest}")
  private lateinit var ingestEndPoint: String

  @Value("\${end-point.ingest-events}")
  private lateinit var ingestEventsEndPoint: String

  @GetMapping(value = ["/subscribe/{topic}"])
  fun subscribe(@PathVariable topic: String) {
    LOG.info("Received subscription request for: $topic")
    val snsClient = buildSnsClient()
    if (topic.contains("file-upload")) {
      val subscribeRequest = SubscribeRequest.builder()
        .topicArn(fileUploadTopicArn)
        .protocol("http")
        .endpoint(ingestEndPoint)
        .build()

      val subscribeResponse = snsClient.subscribe(subscribeRequest)
      LOG.info("Subscribed: ${subscribeResponse.subscriptionArn()}")
    } else if (topic.contains("person")) {
      val subscribeRequest = SubscribeRequest.builder()
        .topicArn(eventsTopicArn)
        .protocol("http")
        .endpoint(ingestEventsEndPoint)
        .build()

      val subscribeResponse = snsClient.subscribe(subscribeRequest)
      LOG.info("Subscribed: ${subscribeResponse.subscriptionArn()}")
    }
  }

  private fun buildSnsClient(): SnsClient = SnsClient.builder()
    .endpointOverride(URI.create(snsEndpoint))
    .region(Region.of(region))
    .build()
}
