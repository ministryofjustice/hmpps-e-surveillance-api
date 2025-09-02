plugins {
  id("uk.gov.justice.hmpps.gradle-spring-boot") version "8.3.2"
  id("org.jlleitschuh.gradle.ktlint") version "11.6.1"
  kotlin("plugin.spring") version "2.1.21"
  kotlin("plugin.serialization") version "1.9.10"
}

dependencyCheck {
  suppressionFile = "/src/main/resources/dependency-suppression.xml"
}

configurations {
  testImplementation { exclude(group = "org.junit.vintage") }
}

dependencies {
  implementation("uk.gov.justice.service.hmpps:hmpps-kotlin-spring-boot-starter:1.5.0")
  implementation("uk.gov.justice.service.hmpps:hmpps-sqs-spring-boot-starter:5.4.10")
  implementation("org.springframework.boot:spring-boot-starter-webflux")
  implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.9")
  implementation("software.amazon.awssdk:s3:2.31.63")
  implementation("software.amazon.awssdk:sts:2.31.63")
  implementation("org.springframework.boot:spring-boot-starter-data-jpa")
  implementation("org.postgresql:postgresql")
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
  implementation("software.amazon.awssdk:sns:2.32.7")
  implementation("com.github.doyaaaaaken:kotlin-csv-jvm:1.8.0")
  implementation("org.flywaydb:flyway-core:11.10.5")
  implementation("jakarta.annotation:jakarta.annotation-api:2.1.1")
  runtimeOnly("org.flywaydb:flyway-database-postgresql:11.10.5")
  testImplementation("com.h2database:h2")
  testImplementation("org.testcontainers:localstack:1.21.3")
  testImplementation("uk.gov.justice.service.hmpps:hmpps-kotlin-spring-boot-starter-test:1.5.0")
  testImplementation("org.wiremock:wiremock-standalone:3.13.1")
  testImplementation("io.swagger.parser.v3:swagger-parser:2.1.30") {
    exclude(group = "io.swagger.core.v3")
  }
}

kotlin {
  jvmToolchain(21)
}

tasks {
  withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    compilerOptions.jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21
  }
}
