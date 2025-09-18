package uk.gov.justice.digital.hmpps.esurveillanceapi.repository

import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import uk.gov.justice.digital.hmpps.esurveillanceapi.entity.Notification

class NotificationRepositoryImpl(
  @PersistenceContext private val entityManager: EntityManager
) : NotificationRepositoryCustom {

  override fun searchNotifications(search: String, pageable: Pageable): Page<Notification> {
    val sql = """
        SELECT * FROM notifications
        WHERE search_vector @@ to_tsquery('english', :search)
        OFFSET :offset LIMIT :limit
    """

    val countSql = """
        SELECT count(*) FROM notifications
        WHERE search_vector @@ to_tsquery('english', :search)
    """

    val searchQuery = search.trim()
      .split("\\s+".toRegex())
      .filter { it.isNotBlank() }
      .joinToString(" & ") { "${it}:*" }

    val query = entityManager.createNativeQuery(sql, Notification::class.java)
      .setParameter("search", searchQuery)
      .setParameter("offset", pageable.offset)
      .setParameter("limit", pageable.pageSize)

    val results = query.resultList as List<Notification>

    val countQuery = entityManager.createNativeQuery(countSql)
      .setParameter("search", searchQuery)

    val total = (countQuery.singleResult as Number).toLong()

    return PageImpl(results, pageable, total)
  }


}
