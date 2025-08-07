package uk.gov.justice.digital.hmpps.esurveillanceapi.messages

import uk.gov.justice.digital.hmpps.esurveillanceapi.service.MessageTemplate

val missingCurfewTemplate = MessageTemplate(
  stern = "[Name]\n\nYour tag suggests you are not in your curfew area when you are supposed to be.\n\nPlease go to your curfew area now. If you do not, this could become a breach of your conditions.\n\nWe will investigate this, and your probation officer will discuss this with you at your next meeting.",
  neutral = "[Name]\n\nYour tag suggests you are not in your curfew area when you are supposed to be.\n\nPlease go to your curfew area now.\n\nYour probation officer will discuss this with you at your next meeting.",
  supportive = "Dear [name]\n\nYour tag suggests you are not in your curfew area when you are supposed to be.\n\nWe understand you may have made a mistake, even by accident. Please go to your curfew area now and we may be able to support you.\n\nYour probation officer will discuss this with you at your next meeting. For more information, call 0800 137 291.",
)
