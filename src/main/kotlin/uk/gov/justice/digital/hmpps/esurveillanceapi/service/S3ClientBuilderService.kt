package uk.gov.justice.digital.hmpps.esurveillanceapi.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.S3Configuration
import java.net.URI

@Service
class S3ClientBuilderService {

  @Value("\${aws.s3.endpoint}")
  private lateinit var s3Endpoint: String

  @Value("\${aws.credentials.accessKey}")
  private lateinit var accessKey: String

  @Value("\${aws.credentials.secretKey}")
  private lateinit var secretKey: String

  @Value("\${aws.region}")
  private lateinit var region: String

  fun buildS3Client(): S3Client = S3Client.builder()
    .region(Region.of(region))
    .endpointOverride(URI.create(s3Endpoint))
    .credentialsProvider(
      StaticCredentialsProvider.create(
        AwsBasicCredentials.create(accessKey, secretKey),
      ),
    )
    .serviceConfiguration(
      S3Configuration.builder()
        .pathStyleAccessEnabled(true)
        .build(),
    )
    .build()
}
