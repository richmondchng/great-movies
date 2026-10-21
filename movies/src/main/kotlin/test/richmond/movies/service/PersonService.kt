package test.richmond.movies.service

import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import test.richmond.movies.data.entity.CastJobEntity
import test.richmond.movies.data.entity.CrewJobEntity
import test.richmond.movies.data.entity.JobEntity
import test.richmond.movies.data.entity.PersonEntity
import test.richmond.movies.data.repository.PersonRepository
import test.richmond.movies.model.dto.ActedIn
import test.richmond.movies.model.dto.CastDTO
import test.richmond.movies.model.dto.CrewDTO
import test.richmond.movies.model.dto.PersonDTO
import test.richmond.movies.model.dto.PersonInput
import test.richmond.movies.model.dto.PersonsDTO
import test.richmond.movies.model.dto.WorkedOn

@Service
class PersonService(
    val personRepository: PersonRepository
) {

    fun getPersons(page: Int, pageSize: Int): PersonsDTO {
        val persons = personRepository.findAll(PageRequest.of(page, pageSize))
        return PersonsDTO(
            persons = persons.stream().map { p -> mapToDTO(p) }.toList(),
            page = page,
            pageSize = pageSize,
            numberOfElements = persons.numberOfElements,
            totalPages = persons.totalPages,
            totalElements = persons.totalElements
        )
    }

    fun getPersonById(id: Long) : PersonDTO? {
        val personEntity = personRepository.findById(id)
        if(personEntity.isPresent) {
            return mapToDTO(personEntity.get())
        }
        return null
    }

    fun addPerson(person: PersonInput): PersonDTO {
        val result = personRepository.save(mapToEntity(person))
        return mapToDTO(result)
    }

    private fun mapToDTO(person: PersonEntity): PersonDTO {
        val workedOn = mutableListOf<WorkedOn>()
        val actedIn = mutableListOf<ActedIn>()

        person.jobs.stream()
            .sorted(Comparator.comparing<JobEntity, Int> { j -> j.movie.releaseYear}.reversed())
            .forEach { job ->
                when (job) {
                    is CrewJobEntity -> workedOn.add(mapToDTO(job))
                    is CastJobEntity -> actedIn.add(mapToDTO(job))
                }
            }

        return PersonDTO(
            id = person.id,
            firstName = person.firstName,
            lastName = person.lastName,
            title = person.title,
            workedOn = workedOn,
            actedIn = actedIn
        )
    }

    private fun mapToEntity(person: PersonInput) : PersonEntity {
        return PersonEntity(
            firstName = person.firstName,
            lastName = person.lastName,
            title = person.title,
        )
    }

    private fun mapToDTO(crewJobEntity: CrewJobEntity) : WorkedOn {
        return WorkedOn(
            jobId = crewJobEntity.id,
            jobName = crewJobEntity.jobName,
            movieId = crewJobEntity.movie.id,
            movieName = crewJobEntity.movie.title,
            releaseYear = crewJobEntity.movie.releaseYear
        )
    }

    private fun mapToDTO(castJobEntity: CastJobEntity) : ActedIn {
        return ActedIn(
            jobId = castJobEntity.id,
            characterName = castJobEntity.characterName,
            movieId = castJobEntity.movie.id,
            movieName = castJobEntity.movie.title,
            releaseYear = castJobEntity.movie.releaseYear
        )
    }
}
