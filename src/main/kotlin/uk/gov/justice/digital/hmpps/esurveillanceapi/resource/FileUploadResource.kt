package uk.gov.justice.digital.hmpps.esurveillanceapi.resource

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import uk.gov.justice.digital.hmpps.esurveillanceapi.service.S3UploadService
import java.net.URL

@RestController
class FileUploadResource(private val s3UploadService: S3UploadService) {
  companion object {
    val LOG: Logger = LoggerFactory.getLogger(this::class.java)
  }

  @GetMapping("/get-upload-url")
  fun generatePresignedUrl(
    @RequestParam filename: String,
  ): URL {
    LOG.info("Generating url for {}", filename)
    return s3UploadService.getUploadUrl(filename)
  }
}
