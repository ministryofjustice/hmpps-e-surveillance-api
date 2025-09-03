package uk.gov.justice.digital.hmpps.esurveillanceapi.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest
import java.net.URL
import java.time.Duration

@Service
class S3UploadService(
  private val s3Client: S3Client,
  private val s3Presigner: S3Presigner,
  @Value("\${aws.s3.peopleAndEventsBucket}") private val bucketName: String,
  @Value("\${aws.s3.urlExpirationMinutes}") private val expirationMinutes: Long
) {

  fun uploadFile(file: MultipartFile, key: String): String {
    val request = PutObjectRequest.builder()
      .bucket(bucketName)
      .key(key)
      .contentType(file.contentType)
      .build()

    s3Client.putObject(request, RequestBody.fromBytes(file.bytes))

    return "https://$bucketName.s3.amazonaws.com/$key"
  }

  fun getUploadUrl(key: String): URL {
    val putObjectRequest = PutObjectRequest.builder()
      .bucket(bucketName)
      .key(key)
      .contentType("text/csv")
      .build()

    val request = PutObjectPresignRequest.builder()
      .signatureDuration(Duration.ofMinutes(expirationMinutes))
      .putObjectRequest(putObjectRequest)
      .build()

    val presignedRequest = s3Presigner.presignPutObject(request)
    return presignedRequest.url()
  }
}
