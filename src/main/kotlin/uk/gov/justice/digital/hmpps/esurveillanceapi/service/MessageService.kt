package uk.gov.justice.digital.hmpps.esurveillanceapi.service

import uk.gov.justice.digital.hmpps.esurveillanceapi.messages.messageTemplates

enum class Violation {
  ENTERING_EXCLUSION_ZONE,
  MISSING_CURFEW,
  TAMPERING_WITH_DEVICE,
}

enum class Tone {
  STERN,
  NEUTRAL,
  SUPPORTIVE,
  ;

  companion object {
    fun fromString(value: String?): Tone? = values().find { it.name.equals(value, ignoreCase = true) }
  }
}

data class MessageTemplate(
  val stern: String,
  val neutral: String,
  val supportive: String,
)

fun generateMessage(name: String, violation: Violation, tone: Tone): String {
  val template = messageTemplates[violation]
    ?: return "No message template found for violation: $violation"

  return when (tone) {
    Tone.STERN -> template.stern
    Tone.NEUTRAL -> template.neutral
    Tone.SUPPORTIVE -> template.supportive
  }.replace(Regex("\\[name\\]", RegexOption.IGNORE_CASE), name)
}
