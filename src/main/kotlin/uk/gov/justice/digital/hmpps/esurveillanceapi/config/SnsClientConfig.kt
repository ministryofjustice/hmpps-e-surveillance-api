package uk.gov.justice.digital.hmpps.esurveillanceapi.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.sns.SnsClient
import java.net.URI

@Configuration
class SnsClientConfig {

  @Value("\${aws.region:eu-west-2}")
  private lateinit var region: String

  @Bean
  @Profile("local")
  fun localstackSnsClient(): SnsClient =
    SnsClient.builder()
      .region(Region.of(region))
      .endpointOverride(URI.create("http://localhost:4566"))
      .credentialsProvider(DefaultCredentialsProvider.create())
      .build()

  @Bean
  @Profile("!local")
  fun awsSnsClient(): SnsClient =
    SnsClient.builder()
      .region(Region.of(region))
      .credentialsProvider(DefaultCredentialsProvider.create())
      .build()
}
