package uk.gov.justice.digital.hmpps.esurveillanceapi.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "pop_users")
data class PopUsers(
  @Id //@GeneratedValue(strategy = GenerationType.AUTO)
  val id: Long = 0,
  val deliusId: String,
  val uniqueDeviceWearerId: String,
  val personId: String,
  val givenName: String,
  val familyName: String,
  val alias: String,
  val createdAt: String,
  val toy: Boolean
) {
  constructor() : this(0L, "", "", "", "", "", "", "", false)
}
