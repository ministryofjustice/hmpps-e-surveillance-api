package uk.gov.justice.digital.hmpps.esurveillanceapi.service

import org.springframework.stereotype.Service

data class Person(
  val personId: String,
  val givenName: String,
)

@Service
object PersonService {
  private val people = listOf(
    Person("bb189f62068247908e37a0ec832837a3", "Mohammed"),
    Person("50b17676e7fc4fbaa608fb11311ef7f3", "Connor"),
    Person("30fa7e410d134a7db0d80a19d50604eb", "Daniel"),
    Person("b311342ec76a443ca4c812b3eb65ae93", "Rajdeep"),
    Person("969baca742114e3a88b747f46c346bfd", "Euan"),
    Person("635d7d573ce44dfb92b0dcb4e169dd7b", "Tariq"),
    Person("7cec187cc5124b469cb55311235088be", "Jacob"),
    Person("cb85b366da004e6ba5e780a9ab81c288", "Liam"),
    Person("960404b2674a4cd98f9f68eb717aad3f", "David"),
    Person("76992cae01bc4cb48f9b78b923306ad5", "Amina"),
    Person("c45013e987484a0f9af193cb82352584", "Kwame"),
    Person("d3c891ef8b2741a6bb56a50f709c4295", "Ben"),
    Person("a996385357754b8a83df4247309032f3", "Arjun"),
    Person("f50b2f6f03f343f99fb4fa65bf7a865c", "Tom"),
    Person("5144488bd2c44bb2a0224660a674c262", "Jamie"),
    Person("ff5066b9f62c428e838e1ef1c8eeb21b", "Omar"),
    Person("1ebc15d8ac76467a9231e8e8052a432a", "Samuel"),
    Person("0373510a4d47432eb28e9e34a718844a", "Hassan"),
    Person("0144d9e666954edc9bdd5ee245db583d", "Isla"),
    Person("b3a3e9416056491c8260eaae0617b621", "Nathan"),
  )

  fun getNameByPersonId(personId: String): String? = people.find { it.personId == personId }?.givenName
}
