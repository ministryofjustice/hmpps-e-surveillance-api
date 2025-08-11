package uk.gov.justice.digital.hmpps.esurveillanceapi.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import uk.gov.justice.digital.hmpps.esurveillanceapi.entity.Persons

interface PersonsRepository : JpaRepository<Persons, Long>, JpaSpecificationExecutor<Persons> {
  fun findByPersonId(personId: String): Persons?
}
