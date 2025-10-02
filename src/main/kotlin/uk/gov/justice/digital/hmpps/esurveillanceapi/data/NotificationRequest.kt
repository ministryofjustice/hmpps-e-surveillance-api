package uk.gov.justice.digital.hmpps.esurveillanceapi.data

data class SmsRequest(
  val templateId: String,
  val phoneNumber: String,
  val personalisation: Map<String, String>
)

data class EmailRequest(
  val templateId: String,
  val emailAddress: String,
  val personalisation: Map<String, String>
)