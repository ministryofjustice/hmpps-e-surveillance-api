package uk.gov.justice.digital.hmpps.esurveillanceapi.repository

import org.springframework.data.jpa.repository.JpaRepository
import uk.gov.justice.digital.hmpps.esurveillanceapi.entity.Event

interface EventRepository : JpaRepository<Event, Long> {
  fun findByPersonIdOrderByTimestampAsc(personId: String): List<Event>
}
