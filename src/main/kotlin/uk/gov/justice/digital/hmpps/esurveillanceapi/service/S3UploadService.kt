package uk.gov.justice.digital.hmpps.esurveillanceapi.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest
import java.net.URL
import java.time.Duration

@Service
class S3UploadService(
  private val s3Presigner: S3Presigner,
  @Value("\${aws.s3.peopleAndEventsBucket}") private val bucketName: String,
) {

  fun getUploadUrl(key: String): URL {
    val putObjectRequest = PutObjectRequest.builder()
      .bucket(bucketName)
      .key(key)
      .contentType("text/csv")
      .build()

    val request = PutObjectPresignRequest.builder()
      .signatureDuration(Duration.ofMinutes(30))
      .putObjectRequest(putObjectRequest)
      .build()

    val presignedRequest = s3Presigner.presignPutObject(request)
    return presignedRequest.url()
  }
}
