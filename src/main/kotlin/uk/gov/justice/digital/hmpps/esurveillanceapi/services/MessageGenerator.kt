package uk.gov.justice.digital.hmpps.esurveillanceapi.service

enum class Violation {
    ENTERING_EXCLUSION_ZONE,
    MISSING_CURFEW,
    TAMPERING_WITH_DEVICE
}

enum class Tone {
    STERN,
    NEUTRAL,
    SUPPORTIVE;

  companion object {
    fun fromString(value: String?): Tone? {
      return values().find { it.name.equals(value, ignoreCase = true) }
    }
  }
}

data class MessageTemplate(
    val stern: String,
    val neutral: String,
    val supportive: String
)

val messageTemplates = mapOf(
    Violation.ENTERING_EXCLUSION_ZONE to MessageTemplate(
        stern = "[Name]\n" +
          "\n" +
          "Your tag location suggests you are in an area you are not allowed to be in.\n" +
          "\n" +
          "Please leave the area now. If you do not, this could become a breach of your conditions.\n" +
          "\n" +
          "We will investigate this, and your probation officer will discuss this with you at your next meeting.",
        neutral = "[Name]\n" +
          "\n" +
          "Your tag location suggests you are in an area you are not allowed to be in.\n" +
          "\n" +
          "Please leave the area now.\n" +
          "\n" +
          "Your probation officer will discuss this with you at your next meeting.",
        supportive = "Dear [name]\n" +
          "\n" +
          "Your tag location suggests you are in an area you are not allowed to be in.\n" +
          "\n" +
          "We understand you may have made a mistake, even by accident. Please leave the area now and we may be able to support you. \n" +
          "\n" +
          "Your probation officer will discuss this with you at your next meeting. For more information, call 0800 137 291."
    ),
    Violation.MISSING_CURFEW to MessageTemplate(
        stern = "[Name]\n" +
          "\n" +
          "Your tag suggests you are not in your curfew area when you are supposed to be.\n" +
          "\n" +
          "Please go to your curfew area now. If you do not, this could become a breach of your conditions.\n" +
          "\n" +
          "We will investigate this, and your probation officer will discuss this with you at your next meeting.",
        neutral = "[Name]\n" +
          "\n" +
          "Your tag suggests you are not in your curfew area when you are supposed to be.\n" +
          "\n" +
          "Please go to your curfew area now.\n" +
          "\n" +
          "Your probation officer will discuss this with you at your next meeting.",
        supportive = "Dear [name]\n" +
          "\n" +
          "Your tag suggests you are not in your curfew area when you are supposed to be.\n" +
          "\n" +
          "We understand you may have made a mistake, even by accident. Please go to your curfew area now and we may be able to support you.\n" +
          "\n" +
          "Your probation officer will discuss this with you at your next meeting. For more information, call 0800 137 291."
    ),
    Violation.TAMPERING_WITH_DEVICE to MessageTemplate(
        stern = "[Name]\n" +
          "\n" +
          "Your tag suggests possible tampering.\n" +
          "\n" +
          "Please stop now. If it continues, it could become a breach of your conditions.\n" +
          "\n" +
          "We will investigate this, and your probation officer will discuss this with you at your next meeting.",
        neutral = "[Name]\n" +
          "\n" +
          "Your tag suggests possible tampering.\n" +
          "\n" +
          "Please stop now.\n" +
          "\n" +
          "Your probation officer will discuss this with you at your next meeting.",
        supportive = "Dear [name]\n" +
          "\n" +
          "Your tag suggests possible tampering.\n" +
          "\n" +
          "We understand you may have made a mistake, even by accident. Please stop now and we may be able to support you. \n" +
          "\n" +
          "Your probation officer will discuss this with you at your next meeting. For more information, call 0800 137 291."
    )
)

fun generateMessage(name: String, violation: Violation, tone: Tone): String {
    val template = messageTemplates[violation]
        ?: return "No message template found for violation: $violation"

    return  when (tone) {
        Tone.STERN -> template.stern
        Tone.NEUTRAL -> template.neutral
        Tone.SUPPORTIVE -> template.supportive
    }
    .replace(Regex("\\[name\\]", RegexOption.IGNORE_CASE), name)
}

