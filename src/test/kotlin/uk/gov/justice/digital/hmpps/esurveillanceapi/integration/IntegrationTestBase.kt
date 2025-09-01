package uk.gov.justice.digital.hmpps.esurveillanceapi.integration

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.http.HttpHeaders
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.reactive.server.WebTestClient
import software.amazon.awssdk.services.sqs.SqsAsyncClient
import software.amazon.awssdk.services.sqs.model.PurgeQueueRequest
import uk.gov.justice.digital.hmpps.esurveillanceapi.integration.wiremock.HmppsAuthApiExtension
import uk.gov.justice.digital.hmpps.esurveillanceapi.integration.wiremock.HmppsAuthApiExtension.Companion.hmppsAuth
import uk.gov.justice.digital.hmpps.hmppstemplatepackagename.integration.testcontainers.LocalStackContainer
import uk.gov.justice.digital.hmpps.hmppstemplatepackagename.integration.testcontainers.LocalStackContainer.setLocalStackProperties
import uk.gov.justice.hmpps.sqs.HmppsQueueService
import uk.gov.justice.hmpps.sqs.MissingQueueException
import uk.gov.justice.hmpps.test.kotlin.auth.JwtAuthorisationHelper

@ExtendWith(HmppsAuthApiExtension::class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
abstract class IntegrationTestBase {

  @BeforeEach
  fun `clear queues`() {
    fileuploadSqsClient.purgeQueue(PurgeQueueRequest.builder().queueUrl(fileuploadQueueUrl).build()).get()
    fileuploadSqsDlqClient.purgeQueue(PurgeQueueRequest.builder().queueUrl(fileuploadDlqUrl).build()).get()
    personidSqsClient.purgeQueue(PurgeQueueRequest.builder().queueUrl(personidQueueUrl).build()).get()
    personidSqsDlqClient.purgeQueue(PurgeQueueRequest.builder().queueUrl(personidDlqUrl).build()).get()
  }

  private val fileuploadQueue by lazy { hmppsQueueService.findByQueueId("fileuploadqueue") ?: throw MissingQueueException("HmppsQueue fileuploadqueue not found") }
  private val personidQueue by lazy { hmppsQueueService.findByQueueId("personidqueue") ?: throw MissingQueueException("HmppsQueue personidqueue not found") }
  private val fileuploadTopic by lazy { hmppsQueueService.findByTopicId("fileuploadtopic") ?: throw MissingQueueException("HmppsTopic fileuploadtopic not found") }
  protected val personidTopic by lazy { hmppsQueueService.findByTopicId("personidtopic") ?: throw MissingQueueException("HmppsTopic personidtopic not found") }

  protected val fileuploadSqsClient by lazy { fileuploadQueue.sqsClient }
  protected val fileuploadSqsDlqClient by lazy { fileuploadQueue.sqsDlqClient as SqsAsyncClient }
  protected val personidSqsClient by lazy { fileuploadQueue.sqsClient }
  protected val personidSqsDlqClient by lazy { personidQueue.sqsDlqClient as SqsAsyncClient }
  protected val personidSnsClient by lazy { personidTopic.snsClient }

  protected val fileuploadQueueUrl by lazy { fileuploadQueue.queueUrl }
  protected val fileuploadDlqUrl by lazy { fileuploadQueue.dlqUrl as String }
  protected val personidQueueUrl by lazy { personidQueue.queueUrl }
  protected val personidDlqUrl by lazy { personidQueue.dlqUrl as String }

  protected val fileuploadTopicArn by lazy { fileuploadTopic.arn }

  @Autowired
  protected lateinit var webTestClient: WebTestClient

  @Autowired
  protected lateinit var jwtAuthHelper: JwtAuthorisationHelper

  @Autowired
  protected lateinit var hmppsQueueService: HmppsQueueService

  internal fun setAuthorisation(
    username: String? = "AUTH_ADM",
    roles: List<String> = listOf(),
    scopes: List<String> = listOf("read"),
  ): (HttpHeaders) -> Unit = jwtAuthHelper.setAuthorisationHeader(username = username, scope = scopes, roles = roles)

  protected fun stubPingWithResponse(status: Int) {
    hmppsAuth.stubHealthPing(status)
  }

  companion object {
    private val localStackContainer = LocalStackContainer.instance

    @JvmStatic
    @DynamicPropertySource
    fun testcontainers(registry: DynamicPropertyRegistry) {
      localStackContainer?.also { setLocalStackProperties(it, registry) }
    }
  }
}
