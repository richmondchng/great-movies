package test.richmond.movies.controller

import org.slf4j.LoggerFactory
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import test.richmond.movies.model.dto.PersonDTO
import test.richmond.movies.model.dto.PersonInput
import test.richmond.movies.model.dto.PersonsDTO
import test.richmond.movies.service.PersonService

@Controller
class PersonController(
    val personService: PersonService
) {
    private val logger = LoggerFactory.getLogger(PersonController::class.java)

    @QueryMapping(name = "persons")
    fun persons(@Argument page: Int, @Argument pageSize: Int) : PersonsDTO {
        logger.info("Get all persons")
        return personService.getPersons(page, pageSize)
    }

    @QueryMapping(name = "person")
    fun person(@Argument id: Long) : PersonDTO? {
        logger.info("Get person by id $id")
        return personService.getPersonById(id)
    }

    @MutationMapping(name = "addPerson")
    fun addPerson(@Argument person: PersonInput) : PersonDTO {
        logger.info("Add person by id $person")
        return personService.addPerson(person)
    }
}