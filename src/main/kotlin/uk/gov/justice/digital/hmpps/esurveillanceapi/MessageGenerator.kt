enum class Violation {
    ENTERING_EXCLUSION_ZONE,
    MISSING_CURFEW,
    TAMPERING_WITH_DEVICE
}

enum class Tone {
    STERN,
    NEUTRAL,
    SUPPORTIVE
}

data class MessageTemplate(
    val stern: String,
    val neutral: String,
    val supportive: String
)

val messageTemplates = mapOf(
    Violation.ENTERING_EXCLUSION_ZONE to MessageTemplate(
        stern = "[name]\n\nYour tag location suggests you are in an area you are not allowed to be in.?\n\nPlease leave that area immediately. If you don't, this could become a breach of your conditions.?\n\nWe will investigate this, and your probation officer will discuss this with you at your next meeting.?",
        neutral = "[name]\n\nYour tag location suggests you are in an area you are not allowed to be in.?\n\nPlease leave that area immediately.?\n\nYour probation officer will discuss this with you at your next meeting.?",
        supportive = "Dear [name]\n\nYour tag location suggests you are in an area you are not allowed to be in.?\n\nWe understand you may have made a mistake, even by accident. Please leave that area immediately and we may be able to support you.? \n\nYour probation officer will discuss this with you at your next meeting.? For more information, contact xxx."
    ),
    Violation.MISSING_CURFEW to MessageTemplate(
        stern = "[name]\n\nYour tag location suggests you have missed your curfew.\n\nPlease return to your curfew area immediately. If you don't, this could become a breach of your conditions.?\n\nWe will investigate this, and your probation officer will discuss this with you at your next meeting.?",
        neutral = "[name]\n\nYour tag location suggests you have missed your curfew.\n\nPlease return to your curfew area immediately.?\n\nYour probation officer will discuss this with you at your next meeting.?",
        supportive = "Dear [name]\n\nYour tag location suggests you have missed your curfew.\n\nWe understand you may have made a mistake, even by accident. Please return to your curfew area immediately and we may be able to support you.\n\nYour probation officer will discuss this with you at your next meeting.? For more information, contact xxx."
    ),
    Violation.TAMPERING_WITH_DEVICE to MessageTemplate(
        stern = "[name]\n\nYour tag data suggests you have been tampering with your ankle tag.\n\nPlease stop doing so immediately.? If you don't, this could become a breach of your conditions.?\n\nWe will investigate this, and your probation officer will discuss this with you at your next meeting.?",
        neutral = "[name]\n\nYour tag data suggests you have been tampering with your ankle tag.\n\nPlease stop doing so immediately.?\n\nYour probation officer will discuss this with you at your next meeting.?",
        supportive = "Dear [name]\n\nYour tag data suggests you have been tampering with your ankle tag.\n\nWe understand you may have made a mistake, even by accident. Please stop doing so immediately and we may be able to support you.? \n\nYour probation officer will discuss this with you at your next meeting.? For more information, contact xxx."
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

