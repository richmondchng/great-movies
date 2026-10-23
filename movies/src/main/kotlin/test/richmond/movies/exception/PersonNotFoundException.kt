package test.richmond.movies.exception

class PersonNotFoundException(
    val personId: String
): RuntimeException(("Person not found with id: $personId"))