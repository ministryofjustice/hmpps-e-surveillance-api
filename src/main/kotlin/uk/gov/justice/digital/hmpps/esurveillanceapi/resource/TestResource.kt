package uk.gov.justice.digital.hmpps.esurveillanceapi.resource

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import uk.gov.justice.digital.hmpps.esurveillanceapi.data.EmailRequest
import uk.gov.justice.digital.hmpps.esurveillanceapi.data.SmsRequest
import uk.gov.justice.digital.hmpps.esurveillanceapi.service.NotifyService


@RestController
@RequestMapping("/test")
class TestResource(private val notifyService: NotifyService) {

  @GetMapping("/hello")
  fun sayHello(@RequestParam(name = "name", defaultValue = "World") name: String): String {
    return "Hello, $name!"
  }

  @PostMapping("/sms")
  fun sendSms(@RequestBody request: SmsRequest): ResponseEntity<String> {
    val notificationId = notifyService.sendSms(
      request.templateId,
      request.phoneNumber,
      request.personalisation,
    )
    return ResponseEntity.ok("SMS request received with notificationId $notificationId")
  }

  @PostMapping("/email")
  fun sendSms(@RequestBody request: EmailRequest): ResponseEntity<String> {
    val notificationId = notifyService.sendEmail(
      request.templateId,
      request.emailAddress,
      request.personalisation,
    )
    return ResponseEntity.ok("Email request received with notificationId $notificationId")
  }
}
