package uk.gov.justice.digital.hmpps.esurveillanceapi.repository

import org.springframework.data.jpa.repository.JpaRepository
import uk.gov.justice.digital.hmpps.esurveillanceapi.entity.Persons

interface PersonsRepository : JpaRepository<Persons, Long> {
  fun findByPersonId(personId: String): Persons?
}
