import type { PageServerLoad } from './$types';
import { graphql } from '#lib/server/graphql.ts';

interface Movie {
    id: number;
    title: string;
    summary: string;
    posterUrl: string | null;
    releaseYear: number | null;
    rating: string | null;
}

interface Movies {
    movies: Movie[]
}

interface MoviesResponse {
    movies: Movies;
    page: number;
    pageSize: number;
    numberOfElements: number;
    totalElements: number;
    totalPages: number;
}

export const load: PageServerLoad = async ({ fetch }) => {
    console.log("Hello from server")
    const data = await graphql<MoviesResponse>(
        fetch,
        `
            query GetMovies($page: Int!, $pageSize: Int!) {
                movies(page: $page, pageSize: $pageSize) {
                    movies {
                        id
                        title
                        releaseYear
                        rating
                    }
                    page
                    pageSize
                    numberOfElements
                    totalElements
                    totalPages
                }
            }
        `,
        { page: 0, pageSize: 10 }
    );

    return {
        movies: data.movies.movies
    };
};