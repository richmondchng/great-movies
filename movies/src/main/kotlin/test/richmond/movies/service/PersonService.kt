package test.richmond.movies.service

import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import test.richmond.movies.data.entity.PersonEntity
import test.richmond.movies.data.repository.PersonRepository
import test.richmond.movies.exception.PersonNotFoundException
import test.richmond.movies.model.dto.PersonDTO

@Service
class PersonService(
    val personRepository: PersonRepository
) {

    fun getPersons(page: Int, pageSize: Int): List<PersonDTO> {
        val persons = personRepository.findAll(PageRequest.of(page, pageSize))
        return persons.stream().map { p -> mapToDto(p) }.toList()
    }

    fun getPersonById(id: Long) : PersonDTO? {
        val personEntity = personRepository.findById(id)
        if(personEntity.isPresent) {
            return mapToDto(personEntity.get())
        }
        return null
    }

    private fun mapToDto(person: PersonEntity): PersonDTO {
        return PersonDTO(
            id = person.id,
            firstName = person.firstName,
            lastName = person.lastName,
            title = person.title,
        )
    }
}
