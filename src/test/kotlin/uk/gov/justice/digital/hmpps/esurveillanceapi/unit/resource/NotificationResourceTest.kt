package uk.gov.justice.digital.hmpps.esurveillanceapi.unit.resource

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import uk.gov.justice.digital.hmpps.esurveillanceapi.entity.Notification
import uk.gov.justice.digital.hmpps.esurveillanceapi.repository.NotificationRepository
import uk.gov.justice.digital.hmpps.esurveillanceapi.resource.NotificationResource
import uk.gov.justice.digital.hmpps.esurveillanceapi.service.NotificationTemplateService
import uk.gov.justice.digital.hmpps.esurveillanceapi.service.NotifyService
import uk.gov.justice.hmpps.test.kotlin.auth.WithMockAuthUser

@WebMvcTest(NotificationResource::class)
class NotificationResourceTest {

  @Autowired
  lateinit var mockMvc: MockMvc

  @MockBean
  lateinit var notificationRepository: NotificationRepository

  @MockBean
  lateinit var notifyService: NotifyService

  @MockBean
  lateinit var notificationTemplateService: NotificationTemplateService

  @Autowired
  lateinit var objectMapper: ObjectMapper

  @WithMockAuthUser(username = "test-user", roles = ["USER"])
  @Test
  fun `should apply search filter`() {
    val spec = mock<Specification<Notification>>()
    val page = PageImpl(emptyList<Notification>(), PageRequest.of(0, 30), 0)

    whenever(notificationRepository.searchNotifications("alert")).thenReturn(spec)
    whenever(notificationRepository.findAll(any<Specification<Notification>>(), any<Pageable>())).thenReturn(page)

    mockMvc.get("/notifications?search=alert")
      .andExpect {
        status { isOk() }
      }

    verify(notificationRepository).searchNotifications("alert")
    verify(notificationRepository).findAll(any<Specification<Notification>>(), any<Pageable>())
  }
}
