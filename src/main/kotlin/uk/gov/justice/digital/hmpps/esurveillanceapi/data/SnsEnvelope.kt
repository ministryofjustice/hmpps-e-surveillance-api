package uk.gov.justice.digital.hmpps.esurveillanceapi.data

data class SnsEnvelope(
  val Type: String,
  val Message: String,
  val SubscribeURL: String? = null
)

data class S3NotificationEvent(
  val Records: List<S3Record>
)

data class S3Record(
  val s3: S3Entity
)

data class S3Entity(
  val bucket: Bucket,
  val `object`: S3Object
)

data class Bucket(val name: String)
data class S3Object(val key: String)