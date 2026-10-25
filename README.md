# Great-Movies (v1.0)

## Project Notes:
- This repository consist of 3 projects; `movies` and `file-mgt` for backend, and `frontend` for frontend
- Source root will be in each folder; i.e., if opening in IDE, open `great-movies/movies`, `great-movies/file-mgt` and `great-movies/frontend`
- Backend is done in Kotlin with Spring Boot 4.1 (Java 17)
  - `movies` service 
	- provide GraphQL endpoints for `frontend` application to query data (and to add data)
  - `file-mgt` service 
	- provide gRPC commands for `movies` to run command to get list of files associated with a movie or person
	- endpoint to serve image to client
	- endpoint to upload file to server; current implementation is to upload to a local directory, but can be extended to manage file in different file system with minimal impact on service logic
- Frontend is Svelte 5 with SvelteKit 3

### Use of AI
The following are the areas assisted by AI
1. Generating SQL for flyway
2. JPA entities configuration - extend entities 
3. Frontend CSS
4. Frontend code snippets
5. Help to understand concept with relevant examples


## Set up and Run (Backend)
1. Go to respective projects' root (`great-movies/movies`, `great-movies/file-mgt`)
2. Build application
   ```shell
   ./gradlew clean build -x test
   ```
3. Run the application
   ```shell
   ./gradlew bootRun
   ```
4. Each backend service will start up the relevant containers from `compose.yml`. This will be the postgres database
5. Refer to respective project's application properties and/or compose file for details.
6. Starting the application in `dev` profile will enable the GraphQL UI (for `movies` project only
   ```shell
   ./gradlew bootrun --args="--spring.profiles.active=dev"
   ```

## Set up and Run (Frontend)
1. Go to `great-movies/frontend`
2. Install dependencies
   ```shell
   npm install
   ```
3. Start application
   ```shell
   npm run dev
   ```
4. Open browser, and go to http://localhost:5173

## Demo
- Starting the backend application will inject some demo data
- This includes movie details like summary, cast and crew, and some photo gallery
- Homepage - shows latest 9 movies (ordered by release date descending)
	- on click to display movie details
- Movies - shows all movies order, loads 10 movies at once, will continue querying 10 movies when reach end of page
	- on click will display movie details
	- click "Add Movie Image" to select an image to upload, select image as "cover" (cover photo for movie) or "gallery" (photo gallery)
- Cast and Crew -> TODO

## GraphQL Query

### Get list of movies
```graphql
query GetMovies {
  movies(page: 0, pageSize: 10, sortBy: "releaseDate", sortAsc: true) {
    movies {
      id
      title
      coverImageUrl {
          id
          url
      }
      images {
          id
          url
      }
      releaseYear
      releaseDate
      rating
      cast {
          personId
          firstName
          lastName
          sequence
          characterName
          coverImageUrl {
              id
              url
          }
      }
      crew {
          personId
          firstName
          lastName
          sequence
          jobName
          coverImageUrl {
              id
              url
          }
      }
    }
    page
    pageSize
    numberOfElements
    totalElements
    totalPages
  }
}
```

### Get Movie by Id
```graphql
query myMovie {
  movie(id: "66d75c91-de01-40f0-865f-7747bda08143") {
      id
      title
      coverImageUrl {
          id
          url
      }
      images {
          id
          url
      }
      releaseYear
      releaseDate
      rating
      cast {
          personId
          firstName
          lastName
          sequence
          characterName
          coverImageUrl {
              id
              url
          }
      }
      crew {
          personId
          firstName
          lastName
          sequence
          jobName
          coverImageUrl {
              id
              url
          }
      }
  }
}
```

### Add Movie
```graphql
mutation {
  addMovie(movie: {
    title: "Movie Title"
    summary: "Movie summary goes here"
    releaseYear: 2011
    rating: "7.0" 
    releaseDate: "1998-10-20"
    }) {
      id
      title
      summary
      rating
      releaseYear
    }
}
```

### Get all People (cast and crew)
```graphql
query {
  persons(page: 1, pageSize: 5) {
      persons {
      id
      firstName
      lastName
      title
      coverImageUrl {
        id
        url
      }
      actedIn {
        jobId
        movieId
        movieName
        releaseYear
        characterName
      }
      workedOn {
        jobId
        movieId
        movieName
        releaseYear
        jobName
      }
    }
    page
    pageSize
    numberOfElements
    totalElements
    totalPages
  }
}
```

### Get People details
```graphql
query {
  person(id: "6233bc18-7329-43f5-b665-900a4dcdba86") {
    id
    firstName
    lastName
    title
    coverImageUrl {
        id
        url
    }
    workedOn {
        jobId
        jobName
        movieId
        movieName
        releaseYear
    }
    actedIn {
        jobId
        characterName
        movieId
        movieName
        releaseYear
    }
  }
}
```

### Add Person
```graphql
mutation {
  addPerson(
    person: {
      firstName: "John"
      lastName: "Little"
      title: "Actor"
    }
  ) {
    id
    firstName
    lastName
    title
  }
  
}
```