package uk.gov.justice.digital.hmpps.esurveillanceapi.unit.resource

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import uk.gov.justice.digital.hmpps.esurveillanceapi.resource.FileUploadResource
import uk.gov.justice.digital.hmpps.esurveillanceapi.service.S3UploadService
import uk.gov.justice.hmpps.test.kotlin.auth.WithMockAuthUser
import java.net.URL

@ExtendWith(SpringExtension::class)
@WebMvcTest(FileUploadResource::class)
class FileUploadResourceTest {

  @Autowired
  lateinit var mockMvc: MockMvc

  @MockBean
  lateinit var s3UploadService: S3UploadService

  @WithMockAuthUser(username = "test-user", roles = ["USER"])
  @Test
  fun `should return presigned url for given filename`() {
    val filename = "test-file.txt"
    val mockUrl = URL("https://s3.amazonaws.com/bucket/$filename")
    `when`(s3UploadService.getUploadUrl(filename)).thenReturn(mockUrl)

    mockMvc.perform(
      get("/get-upload-url")
        .param("filename", filename),
    )
      .andExpect(status().isOk)
      .andExpect(content().json("\"https://s3.amazonaws.com/bucket/test-file.txt\""))

    verify(s3UploadService, times(1)).getUploadUrl(filename)
  }
}
