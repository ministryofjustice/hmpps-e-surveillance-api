package uk.gov.justice.digital.hmpps.esurveillanceapi.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.S3Configuration
import java.net.URI

@Configuration
class S3ClientConfig {

  @Value("\${aws.region:eu-west-2}")
  private lateinit var region: String

  @Bean
  @Profile("local")
  fun localstackS3Client(): S3Client = S3Client.builder()
    .region(Region.of(region))
    .endpointOverride(URI.create("http://localhost:4566"))
    .credentialsProvider(DefaultCredentialsProvider.create())
    .serviceConfiguration(
      S3Configuration.builder()
        .pathStyleAccessEnabled(true)
        .build(),
    )
    .build()

  @Bean
  @Profile("!local")
  fun awsS3Client(): S3Client = S3Client.builder()
    .region(Region.of(region))
    .credentialsProvider(DefaultCredentialsProvider.create())
    .build()
}
