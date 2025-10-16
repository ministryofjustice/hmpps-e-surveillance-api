package uk.gov.justice.digital.hmpps.esurveillanceapi.unit.service

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import uk.gov.justice.digital.hmpps.esurveillanceapi.data.Violation
import uk.gov.justice.digital.hmpps.esurveillanceapi.data.ViolationType
import uk.gov.justice.digital.hmpps.esurveillanceapi.entity.Event
import uk.gov.justice.digital.hmpps.esurveillanceapi.service.ViolationDetector

class ViolationDetectorTest {

  private val detector = ViolationDetector()

  private fun event(name: String) = Event(
    personId = "12345",
    eventName = name,
    timestamp = "2025-10-14T10:00:00Z",
  )

  @Test
  fun `should detect active EXCLUSION_ZONE_VIOLATION`() {
    val events = listOf(
      event("EV_EXCLUDED_PID_ARRIVED_DURING_EXCLUSION"),
    )

    val result = detector.detectViolation(events)

    assertEquals(ViolationType.EXCLUSION_ZONE_VIOLATION, result)
  }

  @Test
  fun `should detect active BATTERY_LOW violation`() {
    val events = listOf(
      event("EV_BATTERY_LEV_5PERCENT"),
    )

    val result = detector.detectViolation(events)

    assertEquals(ViolationType.BATTERY_LOW, result)
  }

  @Test
  fun `should return null if violation is terminated`() {
    val events = listOf(
      event("EV_BATTERY_LEV_5PERCENT"),
      event("EV_CHARGING_STARTED"),
    )

    val result = detector.detectViolation(events)

    assertNull(result)
  }

  @Test
  fun `should return first active violation if multiple are active`() {
    val events = listOf(
      event("EV_PID_ABSENT"),
      event("EV_BATTERY_LEV_5PERCENT"),
    )

    val result = detector.detectViolation(events)

    assertEquals(ViolationType.CURFEW_VIOLATION, result)
  }

  @Test
  fun `should return null if no known initiators`() {
    val events = listOf(
      event("EV_UNKNOWN_EVENT"),
    )

    val result = detector.detectViolation(events)

    assertNull(result)
  }

  @Test
  fun `should correctly map ViolationType to Violation`() {
    assertEquals(
      Violation.ENTERING_EXCLUSION_ZONE,
      detector.mapViolationTypeToViolation(ViolationType.EXCLUSION_ZONE_VIOLATION),
    )

    assertEquals(
      Violation.BATTERY_LOW,
      detector.mapViolationTypeToViolation(ViolationType.BATTERY_LOW),
    )

    assertEquals(
      Violation.MISSING_CURFEW,
      detector.mapViolationTypeToViolation(ViolationType.CURFEW_VIOLATION),
    )

    assertEquals(
      Violation.TAMPERING_WITH_DEVICE,
      detector.mapViolationTypeToViolation(ViolationType.TAMPER),
    )
  }
}
