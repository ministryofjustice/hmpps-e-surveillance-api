package uk.gov.justice.digital.hmpps.esurveillanceapi.unit.resource

import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Test
import org.mockito.Mockito.any
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import uk.gov.justice.digital.hmpps.esurveillanceapi.entity.Persons
import uk.gov.justice.digital.hmpps.esurveillanceapi.repository.PersonsRepository
import uk.gov.justice.digital.hmpps.esurveillanceapi.resource.PersonsResource
import uk.gov.justice.hmpps.test.kotlin.auth.WithMockAuthUser
import java.time.LocalDateTime

@WebMvcTest(PersonsResource::class)
class PersonsResourceTest {

  @Autowired
  lateinit var mockMvc: MockMvc

  @MockBean
  lateinit var personsRepository: PersonsRepository

  @WithMockAuthUser(username = "test-user", roles = ["USER"])
  @Test
  fun `should return paginated list of persons`() {
    val pageable = PageRequest.of(0, 30)
    val personsList = listOf(
      Persons(1L, "123", "wd123", "p123", "John", "Doe", "john", LocalDateTime.now().toString(), true),
      Persons(2L, "123", "wd124", "p234", "Smith", "Doe", "smith", LocalDateTime.now().toString(), true),
    )
    val page = PageImpl(personsList, pageable, personsList.size.toLong())

    `when`(personsRepository.findAll(any<Specification<Persons>>(), any<Pageable>())).thenReturn(page)

    mockMvc.get("/persons")
      .andExpect {
        status { isOk() }
        content { contentType(MediaType.APPLICATION_JSON) }
        jsonPath("$.content[0].givenName", equalTo("John"))
        jsonPath("$.content[1].givenName", equalTo("Smith"))
      }

    verify(personsRepository).findAll(any<Specification<Persons>>(), any<Pageable>())
  }

  @WithMockAuthUser(username = "test-user", roles = ["USER"])
  @Test
  fun `should apply givenName filter`() {
    val spec = mock(Specification::class.java) as Specification<Persons>
    val page = PageImpl(emptyList<Persons>(), PageRequest.of(0, 30), 0)

    `when`(personsRepository.givenNameContains("John")).thenReturn(spec)
    `when`(personsRepository.findAll(any<Specification<Persons>>(), any<Pageable>())).thenReturn(page)

    mockMvc.get("/persons?givenName=John")
      .andExpect {
        status { isOk() }
      }

    verify(personsRepository).givenNameContains("John")
    verify(personsRepository).findAll(any<Specification<Persons>>(), any<Pageable>())
  }

  @WithMockAuthUser(username = "test-user", roles = ["USER"])
  @Test
  fun `should apply search filter`() {
    val spec = mock(Specification::class.java) as Specification<Persons>
    val page = PageImpl(emptyList<Persons>(), PageRequest.of(0, 30), 0)

    `when`(personsRepository.searchPersons("Smith")).thenReturn(spec)
    `when`(personsRepository.findAll(any<Specification<Persons>>(), any<Pageable>())).thenReturn(page)

    mockMvc.get("/persons?search=Smith")
      .andExpect {
        status { isOk() }
      }

    verify(personsRepository).searchPersons("Smith")
    verify(personsRepository).findAll(any<Specification<Persons>>(), any<Pageable>())
  }

  @WithMockAuthUser(username = "test-user", roles = ["USER"])
  @Test
  fun `should apply multiple filters`() {
    val givenSpec = mock(Specification::class.java) as Specification<Persons>
    val familySpec = mock(Specification::class.java) as Specification<Persons>
    val combinedSpec = mock(Specification::class.java) as Specification<Persons>
    val page = PageImpl(emptyList<Persons>(), PageRequest.of(0, 30), 0)

    `when`(personsRepository.givenNameContains("Jane")).thenReturn(givenSpec)
    `when`(personsRepository.familyNameContains("Doe")).thenReturn(familySpec)
    `when`(givenSpec.and(familySpec)).thenReturn(combinedSpec)
    `when`(personsRepository.findAll(any<Specification<Persons>>(), any<Pageable>())).thenReturn(page)

    mockMvc.get("/persons?givenName=Jane&familyName=Doe")
      .andExpect {
        status { isOk() }
      }

    verify(personsRepository).givenNameContains("Jane")
    verify(personsRepository).familyNameContains("Doe")
    verify(personsRepository).findAll(any<Specification<Persons>>(), any<Pageable>())
  }
}
