package uk.gov.justice.digital.hmpps.esurveillanceapi.resource

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import uk.gov.justice.digital.hmpps.esurveillanceapi.service.NotificationTemplateService
import uk.gov.justice.digital.hmpps.esurveillanceapi.service.NotifyService

@RestController
@RequestMapping("/test")
class TestResource(
  private val notifyService: NotifyService,
  private val notificationTemplateService: NotificationTemplateService,
) {

  @GetMapping("/hello")
  fun sayHello(@RequestParam(name = "name", defaultValue = "World") name: String): String = "Hello, $name!"
}
