package uk.gov.justice.digital.hmpps.esurveillanceapi.repository

import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import uk.gov.justice.digital.hmpps.esurveillanceapi.entity.Persons

class PersonsRepositoryImpl(
  @PersistenceContext private val entityManager: EntityManager,
) : PersonsRepositoryCustom {

  override fun searchPersons(search: String, pageable: Pageable): Page<Persons> {
    val sql = """
        SELECT * FROM persons
        WHERE search_vector @@ to_tsquery('english', :search)
        OFFSET :offset LIMIT :limit
    """

    val countSql = """
        SELECT count(*) FROM persons
        WHERE search_vector @@ to_tsquery('english', :search)
    """

    val searchQuery = search.trim()
      .split("\\s+".toRegex())
      .filter { it.isNotBlank() }
      .joinToString(" & ") { "$it:*" }

    val query = entityManager.createNativeQuery(sql, Persons::class.java)
      .setParameter("search", searchQuery)
      .setParameter("offset", pageable.offset)
      .setParameter("limit", pageable.pageSize)

    val results = query.resultList as List<Persons>

    val countQuery = entityManager.createNativeQuery(countSql)
      .setParameter("search", searchQuery)

    val total = (countQuery.singleResult as Number).toLong()

    return PageImpl(results, pageable, total)
  }
}
