package uk.gov.justice.digital.hmpps.esurveillanceapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ESurveillanceApi

fun main(args: Array<String>) {
  runApplication<ESurveillanceApi>(*args)
}
