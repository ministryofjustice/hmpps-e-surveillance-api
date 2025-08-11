package uk.gov.justice.digital.hmpps.esurveillanceapi.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "persons")
data class Persons(
  @Id
  val id: Long = 0,
  val deliusId: String,
  val uniqueDeviceWearerId: String,
  val personId: String,
  val givenName: String,
  val familyName: String,
  val alias: String,
  val createdAt: String,
  val toy: Boolean,
) {
  constructor() : this(0L, "", "", "", "", "", "", "", false)
}
