import type { Movies, LatestMovieItem, MovieListItem, MoviesResponse, MovieDetailResponse, MovieDetailsItem } from '#lib/types/movie.ts'
import { graphql } from './graphql';

export async function getMovies(
    fetchFn: typeof fetch,
    page: number,
    size = 10
): Promise<Movies<MovieListItem>> {

    const data = await graphql<MoviesResponse<MovieListItem>>(
            fetchFn,
            `
                query GetMovies($page: Int!, $pageSize: Int!) {
                    movies(page: $page, pageSize: $pageSize) {
                        movies {
                            id
                            title
                            releaseYear
                            rating
                            coverImageUrl {
                                id
                                imageUrl
                            }
                        }
                        page
                        pageSize
                        numberOfElements
                        totalElements
                        totalPages
                    }
                }
            `,
            { page: page, pageSize: size }
        );
    
    return data.movies
}

export async function getLatestMovies(
    fetchFn: typeof fetch,
): Promise<Movies<LatestMovieItem>> {

    const data = await graphql<MoviesResponse<LatestMovieItem>>(
            fetchFn,
            `
                query GetMovies($page: Int!, $pageSize: Int!, $sortBy: String!, $sortAsc: Boolean!) {
                    movies(page: $page, pageSize: $pageSize, sortBy: $sortBy, sortAsc: $sortAsc) {
                        movies {
                            id
                            title
                            releaseYear
                            rating
                            coverImageUrl {
                                id
                                imageUrl
                            }
                        }
                    }
                }
            `,
            { page: 0, pageSize: 9, sortBy: "releaseDate", sortAsc: false }
        );
    
    return data.movies
}

export async function getMovie(
    fetchFn: typeof fetch,
    id: number
): Promise<MovieDetailsItem> {

    const data = await graphql<MovieDetailResponse>(
            fetchFn,
            `
                query GetMovies($id: Int!) {
                    movie(id: $id) {
                        id
                        title
                        summary
                        coverImageUrl {
                            id
                            imageUrl
                        }
                        images {
                            id
                            imageUrl
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
                                imageUrl
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
                                imageUrl
                            }
                        }
                    }
                }
            `,
            { id: id }
        );
    
    return data.movie
}