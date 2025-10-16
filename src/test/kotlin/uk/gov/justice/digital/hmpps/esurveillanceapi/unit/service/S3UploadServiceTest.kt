package uk.gov.justice.digital.hmpps.esurveillanceapi.unit.service

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest
import uk.gov.justice.digital.hmpps.esurveillanceapi.service.S3UploadService
import java.net.URL
import java.time.Duration

class S3UploadServiceTest {

  private val s3Presigner: S3Presigner = mock()
  private val bucketName = "test-bucket"
  private val expirationMinutes = 15L

  private val service = S3UploadService(s3Presigner, bucketName, expirationMinutes)

  @Test
  fun `should generate pre-signed URL for upload`() {
    val key = "test-file.csv"
    val expectedUrl = URL("https://s3.amazonaws.com/$bucketName/$key")

    val mockPresignedRequest: PresignedPutObjectRequest = mock {
      on { url() } doReturn expectedUrl
    }

    val captor = argumentCaptor<PutObjectPresignRequest>()
    whenever(s3Presigner.presignPutObject(captor.capture())).thenReturn(mockPresignedRequest)

    val result = service.getUploadUrl(key)

    assertEquals(expectedUrl, result)
    val capturedRequest = captor.firstValue

    assertEquals(Duration.ofMinutes(expirationMinutes), capturedRequest.signatureDuration())
    val putObjectRequest: PutObjectRequest = capturedRequest.putObjectRequest()
    assertEquals(bucketName, putObjectRequest.bucket())
    assertEquals(key, putObjectRequest.key())
    assertEquals("text/csv", putObjectRequest.contentType())
  }
}
