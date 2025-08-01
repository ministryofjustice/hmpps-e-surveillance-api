package uk.gov.justice.digital.hmpps.esurveillanceapi.controller

import org.springframework.web.bind.annotation.*
import org.springframework.http.ResponseEntity
import org.springframework.http.HttpStatus
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import uk.gov.justice.digital.hmpps.esurveillanceapi.domain.Event
import uk.gov.justice.digital.hmpps.esurveillanceapi.domain.ViolationRequest
import uk.gov.justice.digital.hmpps.esurveillanceapi.mappers.mapViolationTypeToViolation
import uk.gov.justice.digital.hmpps.esurveillanceapi.service.Tone
import uk.gov.justice.digital.hmpps.esurveillanceapi.service.ViolationDetector
import uk.gov.justice.digital.hmpps.esurveillanceapi.service.generateMessage
import uk.gov.justice.digital.hmpps.esurveillanceapi.service.PersonService

@RestController
@RequestMapping("/detect-violation")
class DetectViolationController(
  private val violationDetector: ViolationDetector,
  private val personService: PersonService
) {

  @PostMapping
  fun detectViolation(@RequestBody req: ViolationRequest): ResponseEntity<Map<String, String?>> {
    val events = req.events
    if (events.isEmpty()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(mapOf("error" to "No events provided"))
    }

    val violationType = violationDetector.detectViolation(events)
    val violation = violationType?.let { mapViolationTypeToViolation(it) }

    val personId = events.firstOrNull()?.personId.orEmpty()
    val popName = personService.getNameByPersonId(personId).orEmpty()

    if (violation == null) {
      return ResponseEntity.ok(
        mapOf(
          "violation" to null,
          "message" to null
        )
      )
    }

    val tone = Tone.fromString(req.tone) ?: Tone.NEUTRAL
    val message = generateMessage(popName, violation, tone)

    val formattedDate = LocalDateTime.now()
      .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))

    return ResponseEntity.ok(
      mapOf(
        "violation" to violation.name,
        "message" to message,
        "date" to formattedDate
      )
    )
  }
}
