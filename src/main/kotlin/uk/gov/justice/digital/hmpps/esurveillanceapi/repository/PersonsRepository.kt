package uk.gov.justice.digital.hmpps.esurveillanceapi.repository

import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import uk.gov.justice.digital.hmpps.esurveillanceapi.entity.Persons

interface PersonsRepository :
  JpaRepository<Persons, Long>,
  JpaSpecificationExecutor<Persons>,
  PersonsRepositoryCustom {

  fun findByPersonId(personId: String): Persons?

  fun givenNameContains(givenName: String): Specification<Persons> = Specification { root, _, cb ->
    cb.like(cb.lower(root.get("givenName")), "%${givenName.lowercase()}%")
  }

  fun familyNameContains(familyName: String): Specification<Persons> = Specification { root, _, cb ->
    cb.like(cb.lower(root.get("familyName")), "%${familyName.lowercase()}%")
  }

  fun personIdEquals(personId: String): Specification<Persons> = Specification { root, _, cb ->
    cb.equal(root.get<String>("personId"), personId)
  }
}
