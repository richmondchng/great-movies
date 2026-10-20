package test.richmond.movies.exception

class PersonNotFoundException(
    val personId: Long
): RuntimeException(("Person not found with id: $personId"))