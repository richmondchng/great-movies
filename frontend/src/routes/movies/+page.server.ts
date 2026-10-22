import type { PageServerLoad } from './$types';
import type { MoviesResponse } from '#lib/types/movie.ts';

import { graphql } from '#lib/server/graphql.ts';

export const load: PageServerLoad = async ({ fetch }) => {
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