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
    @RequestParam(required = false) name: String?,
    @RequestParam(required = false) personId: String?,
    @PageableDefault(size = 30, sort = ["timestamp"], direction = org.springframework.data.domain.Sort.Direction.DESC)
    pageable: Pageable,
  ): Page<Persons> {
    var spec: Specification<Persons>? = null

    if (!name.isNullOrBlank()) {
      spec = spec?.and(nameContains(name))
    }

    if (personId != null) {
      spec = spec?.and(personIdEquals(personId))
    }

    return personsRepository.findAll(spec, pageable)
  }

  fun nameContains(name: String?): Specification<Persons>? {
    return if (name.isNullOrBlank()) null else Specification { root, _, cb ->
      cb.like(cb.lower(root.get("name")), "%${name.lowercase()}%")
    }
  }

  fun personIdEquals(personId: String?): Specification<Persons>? {
    return if (personId == null) null else Specification { root, _, cb ->
      cb.equal(root.get<String>("personId"), personId)
    }
  }
}