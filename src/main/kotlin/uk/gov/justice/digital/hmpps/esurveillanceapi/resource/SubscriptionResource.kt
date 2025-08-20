package uk.gov.justice.digital.hmpps.esurveillanceapi.resource

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/test")
class SubscriptionResource {

  @GetMapping("/hello")
  fun sayHello(@RequestParam(name = "name", defaultValue = "World") name: String): String = "Hello, $name!"
}
