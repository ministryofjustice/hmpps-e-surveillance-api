package uk.gov.justice.digital.hmpps.esurveillanceapi.resource

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
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
    @RequestParam(required = false) search: String?,
    @RequestParam(required = false, name = "sort_by") sortBy: String?,
    @RequestParam(required = false, name = "sort_dir") sortDir: String?,
    @PageableDefault(size = 30, sort = ["createdAt"], direction = org.springframework.data.domain.Sort.Direction.DESC)
    pageable: Pageable,
  ): Page<Persons> {
    var spec: Specification<Persons>? = null
    val validSortFields = setOf("timestamp", "unique_device_wearer_id", "given_name", "family_name", "alias")
    val sort = if (!sortBy.isNullOrBlank() && sortBy in validSortFields) {
      val direction = when (sortDir?.lowercase()) {
        "asc" -> Sort.Direction.ASC
        "desc" -> Sort.Direction.DESC
        else -> Sort.Direction.DESC
      }
      Sort.by(direction, sortBy)
    } else {
      pageable.sort
    }
    val updatedPageable = PageRequest.of(pageable.pageNumber, pageable.pageSize, sort)
    if (!search.isNullOrBlank()) {
      return personsRepository.searchPersons(search, updatedPageable)
    }

    if (!givenName.isNullOrBlank()) {
      spec = personsRepository.givenNameContains(givenName)
    }

    if (!familyName.isNullOrBlank()) {
      spec = spec?.and(personsRepository.familyNameContains(familyName)) ?: personsRepository.familyNameContains(familyName)
    }

    if (!personId.isNullOrBlank()) {
      spec = spec?.and(personsRepository.personIdEquals(personId)) ?: personsRepository.personIdEquals(personId)
    }

    return personsRepository.findAll(spec, updatedPageable)
  }
}
