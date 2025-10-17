package uk.gov.justice.digital.hmpps.esurveillanceapi.unit.service

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.any
import org.mockito.Mockito.anyList
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import software.amazon.awssdk.core.ResponseInputStream
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import software.amazon.awssdk.services.s3.model.GetObjectResponse
import software.amazon.awssdk.services.sns.SnsClient
import software.amazon.awssdk.services.sns.model.PublishRequest
import software.amazon.awssdk.services.sns.model.PublishResponse
import uk.gov.justice.digital.hmpps.esurveillanceapi.entity.Persons
import uk.gov.justice.digital.hmpps.esurveillanceapi.repository.PersonsRepository
import uk.gov.justice.digital.hmpps.esurveillanceapi.service.FileProcessorService
import java.io.ByteArrayInputStream

class FileProcessorServiceTest {

  private lateinit var personsRepository: PersonsRepository
  private lateinit var s3Client: S3Client
  private lateinit var snsClient: SnsClient
  private lateinit var fileProcessorService: FileProcessorService

  private val objectMapper = jacksonObjectMapper()

  @BeforeEach
  fun setup() {
    personsRepository = mock(PersonsRepository::class.java)
    s3Client = mock(S3Client::class.java)
    snsClient = mock(SnsClient::class.java)

    fileProcessorService = FileProcessorService(personsRepository, s3Client, snsClient)

    val field = fileProcessorService.javaClass.getDeclaredField("personIdTopicArn")
    field.isAccessible = true
    field.set(fileProcessorService, "arn:test:person-topic")
  }

  @Test
  fun `processPersons should read CSV and save entities`() {
    val csv = """
            id,delius_id,unique_device_wearer_id,person_id,given_name,family_name,alias,timestamp,toy
            1,'D123','U456','P789','John','Doe','JD','2023-01-01',true
    """.trimIndent()

    val responseStream = ResponseInputStream(GetObjectResponse.builder().build(), ByteArrayInputStream(csv.toByteArray()))
    `when`(s3Client.getObject(any(GetObjectRequest::class.java))).thenReturn(responseStream)

    fileProcessorService.processPersons("test-bucket", "file.csv")

    val captor = ArgumentCaptor.forClass(List::class.java) as ArgumentCaptor<List<Persons>>
    verify(personsRepository).saveAll(captor.capture())

    val savedPersons = captor.value
    assert(savedPersons.size == 1)
    assert(savedPersons[0].deliusId == "D123")
    assert(savedPersons[0].personId == "P789")
  }

  @Test
  fun `processEvents should publish messages to SNS for each unique personId`() {
    val csv = """
            person_id
            P111
            P222
            P111
    """.trimIndent()

    val responseStream = ResponseInputStream(GetObjectResponse.builder().build(), ByteArrayInputStream(csv.toByteArray()))
    `when`(s3Client.getObject(any(GetObjectRequest::class.java))).thenReturn(responseStream)

    val publishResponse = PublishResponse.builder().messageId("msg-123").build()
    `when`(snsClient.publish(any(PublishRequest::class.java))).thenReturn(publishResponse)

    fileProcessorService.processEvents("bucket-x", "events.csv")

    val captor = ArgumentCaptor.forClass(PublishRequest::class.java)
    verify(snsClient, times(2)).publish(captor.capture()) // P111 and P222

    val requests = captor.allValues
    val sentIds = requests.map { it.messageGroupId() }
    assert(sentIds.contains("person-P111"))
    assert(sentIds.contains("person-P222"))
  }

  @Test
  fun `processUploadedFile should parse outer JSON and call internal processor`() {
    val innerMessage = """
            {
              "Records": [
                {
                  "s3": {
                    "bucket": { "name": "my-bucket" },
                    "object": { "key": "person.csv" }
                  }
                }
              ]
            }
    """.trimIndent()

    val wrapped = objectMapper.readTree("""{"Message": ${objectMapper.writeValueAsString(innerMessage)}}""") as JsonNode

    val csv = """
            id,delius_id,unique_device_wearer_id,person_id,given_name,family_name,alias,timestamp,toy
            1,'D123','U456','P789','John','Doe','JD','2023-01-01',true
    """.trimIndent()

    val responseStream = ResponseInputStream(GetObjectResponse.builder().build(), ByteArrayInputStream(csv.toByteArray()))
    `when`(s3Client.getObject(any(GetObjectRequest::class.java))).thenReturn(responseStream)

    assertDoesNotThrow {
      fileProcessorService.processUploadedFile(wrapped)
    }

    verify(personsRepository).saveAll(anyList())
  }
}
