package test.richmond.movies.service

import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import test.richmond.movies.data.entity.PersonEntity
import test.richmond.movies.data.repository.PersonRepository
import test.richmond.movies.model.dto.PersonDTO
import test.richmond.movies.model.dto.PersonsDTO

@Service
class PersonService(
    val personRepository: PersonRepository
) {

    fun getPersons(page: Int, pageSize: Int): PersonsDTO {
        val persons = personRepository.findAll(PageRequest.of(page, pageSize))
        return PersonsDTO(
            persons = persons.stream().map { p -> mapToDTO(p) }.toList(),
            page = page,
            pageSize = pageSize
        )
    }

    fun getPersonById(id: Long) : PersonDTO? {
        val personEntity = personRepository.findById(id)
        if(personEntity.isPresent) {
            return mapToDTO(personEntity.get())
        }
        return null
    }

    private fun mapToDTO(person: PersonEntity): PersonDTO {
        return PersonDTO(
            id = person.id,
            firstName = person.firstName,
            lastName = person.lastName,
            title = person.title,
        )
    }
}
