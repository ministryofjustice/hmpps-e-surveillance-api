package uk.gov.justice.digital.hmpps.esurveillanceapi.messages

import uk.gov.justice.digital.hmpps.esurveillanceapi.service.MessageTemplate

val batteryTemplate = MessageTemplate(
  stern = "Deleted Option",
  neutral = "[Name]  \n\nYour tag battery is at 10% charge.\n\n Please charge it now and we may be able to support you. If you do not, this could become a breach of your conditions.\n\nWe will look into this, and your probation officer will contact you to discuss it further. ",
  supportive = "Dear [name]\n\nYour tag battery is at 10% charge.\n\nWe understand there may be a reason this happened. Please charge it now and we may be able to support you.\n\nYour probation officer will discuss this with you at your next meeting. For more information, call 0800 137 291.",
)
