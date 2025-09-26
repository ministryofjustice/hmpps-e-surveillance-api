package uk.gov.justice.digital.hmpps.esurveillanceapi.resource

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import uk.gov.justice.digital.hmpps.esurveillanceapi.entity.Notification
import uk.gov.justice.digital.hmpps.esurveillanceapi.repository.NotificationRepository

@RestController
@RequestMapping("/notifications")
class NotificationResource(
  private val notificationRepository: NotificationRepository,
) {

  @GetMapping
  fun getNotifications(
    @RequestParam(required = false) search: String?,
    @PageableDefault(size = 30, sort = ["timestamp"], direction = org.springframework.data.domain.Sort.Direction.DESC)
    pageable: Pageable,
  ): Page<Notification> {
    var spec: Specification<Notification>? = null

    if (!search.isNullOrBlank()) {
      spec = notificationRepository.searchNotifications(search)
    }
    return notificationRepository.findAll(spec, pageable)
  }
}
