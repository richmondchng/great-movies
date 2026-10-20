package test.richmond.movies.service

import org.springframework.stereotype.Service
import test.richmond.movies.model.dto.PersonDTO

@Service
class PersonService {

    fun getPersons(): List<PersonDTO> {
        throw NotImplementedError()
    }

    fun getPersonById(id: Long) : PersonDTO? {
        throw NotImplementedError()
    }
}
