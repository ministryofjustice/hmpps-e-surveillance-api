package uk.gov.justice.digital.hmpps.esurveillanceapi.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import uk.gov.justice.digital.hmpps.esurveillanceapi.entity.Persons

interface PersonsRepositoryCustom {
  fun searchPersons(search: String, pageable: Pageable): Page<Persons>
}