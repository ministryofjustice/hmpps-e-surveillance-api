package uk.gov.justice.digital.hmpps.esurveillanceapi.resource

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import uk.gov.justice.digital.hmpps.esurveillanceapi.entity.Persons
import uk.gov.justice.digital.hmpps.esurveillanceapi.repository.PersonsRepository

@RestController
@RequestMapping("/persons")
class PersonsResource(
  private val personsRepository: PersonsRepository,
) {

  @GetMapping
  fun getPersons(
    @RequestParam(required = false) givenName: String?,
    @RequestParam(required = false) familyName: String?,
    @RequestParam(required = false) personId: String?,
    @PageableDefault(size = 30, sort = ["timestamp"], direction = org.springframework.data.domain.Sort.Direction.DESC)
    pageable: Pageable,
  ): Page<Persons> {
    var spec: Specification<Persons>? = null

    if (!givenName.isNullOrBlank()) {
      spec = personsRepository.givenNameContains(givenName)
    }

    if (!familyName.isNullOrBlank()) {
      spec = spec?.and(personsRepository.familyNameContains(familyName)) ?: personsRepository.familyNameContains(familyName)
    }

    if (!personId.isNullOrBlank()) {
      spec = spec?.and(personsRepository.personIdEquals(personId)) ?: personsRepository.personIdEquals(personId)
    }

    return personsRepository.findAll(spec, pageable)
  }

}