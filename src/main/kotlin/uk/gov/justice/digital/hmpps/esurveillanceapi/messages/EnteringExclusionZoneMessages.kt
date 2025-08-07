package uk.gov.justice.digital.hmpps.esurveillanceapi.messages

import uk.gov.justice.digital.hmpps.esurveillanceapi.service.MessageTemplate

val enteringExclusionZoneTemplate = MessageTemplate(
  stern = "[Name]\n\nYour tag location suggests you are in an area you are not allowed to be in.\n\nPlease leave the area now. If you do not, this could become a breach of your conditions.\n\nWe will investigate this, and your probation officer will discuss this with you at your next meeting.",
  neutral = "[Name]\n\nYour tag location suggests you are in an area you are not allowed to be in.\n\nPlease leave the area now.\n\nYour probation officer will discuss this with you at your next meeting.",
  supportive = "Dear [name]\n\nYour tag location suggests you are in an area you are not allowed to be in.\n\nWe understand you may have made a mistake, even by accident. Please leave the area now and we may be able to support you. \n\nYour probation officer will discuss this with you at your next meeting. For more information, call 0800 137 291.",
)
