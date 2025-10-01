package uk.gov.justice.digital.hmpps.esurveillanceapi.data

data class SmsRequest(
  val violation: Violation,
  val phoneNumber: String,
  val personalisation: Map<String, String>
)

data class EmailRequest(
  val violation: Violation,
  val emailAddress: String,
  val personalisation: Map<String, String>
)