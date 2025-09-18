package uk.gov.justice.digital.hmpps.esurveillanceapi.resource

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
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
    @RequestParam(required = false, name = "sort_by") sortBy: String?,
    @RequestParam(required = false, name = "sort_dir") sortDir: String?,
    @PageableDefault(size = 30, sort = ["timestamp"], direction = org.springframework.data.domain.Sort.Direction.DESC)
    pageable: Pageable,
  ): Page<Notification> {
    val validSortFields = setOf("timestamp", "violation", "message", "person_name")
    val sort = if (!sortBy.isNullOrBlank() && sortBy in validSortFields) {
      val direction = when(sortDir?.lowercase()) {
        "asc" -> Sort.Direction.ASC
        "desc" -> Sort.Direction.DESC
        else -> Sort.Direction.DESC
      }
      Sort.by(direction, sortBy)
    } else {
      pageable.sort
    }
    val updatedPageable = PageRequest.of(pageable.pageNumber, pageable.pageSize, sort)
    return if (!search.isNullOrBlank()) {
      notificationRepository.searchNotifications(search, updatedPageable)
    } else {
      notificationRepository.findAll(updatedPageable)
    }
  }
}
