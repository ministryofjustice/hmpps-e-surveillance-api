package uk.gov.justice.digital.hmpps.esurveillanceapi.resource

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import uk.gov.justice.digital.hmpps.esurveillanceapi.service.S3UploadService
import java.net.URL
import java.util.*

@RestController
class FileUploadResource(private val s3UploadService: S3UploadService) {
  companion object {
    val LOG: Logger = LoggerFactory.getLogger(this::class.java)
  }

  @PostMapping("/file/upload")
  fun uploadFile(@RequestParam("file") file: MultipartFile): ResponseEntity<String> {
    val fileName = file.originalFilename ?: UUID.randomUUID().toString()
    LOG.info("Uploading file {}", fileName)
    val s3Url = s3UploadService.uploadFile(file, fileName)
    return ResponseEntity.ok("Uploaded to: $s3Url")
  }

  @GetMapping("/get-upload-url")
  fun generatePresignedUrl(
    @RequestParam filename: String,
  ): URL {
    LOG.info("Generating url for {}", filename)
    return s3UploadService.getUploadUrl(filename)
  }
}
