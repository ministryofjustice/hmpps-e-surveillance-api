package uk.gov.justice.digital.hmpps.esurveillanceapi.data

data class TriggerNotificationRequest(
  val ppGivenName: String,
  val ppFamilyName: String,
  val givenName: String,
  val familyName: String,
  val violationType: Violation,
  val phoneNumber: String,
  val email: String,
)
