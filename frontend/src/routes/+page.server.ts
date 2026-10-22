import type { PageServerLoad } from './$types';
import type { MoviesResponse } from '#lib/types/movie.ts';

import { graphql } from '#lib/server/graphql.ts';

export const load: PageServerLoad = async ({ fetch }) => {
    const data = await graphql<MoviesResponse>(
        fetch,
        `
            query GetMovies($page: Int!, $pageSize: Int!, $sortBy: String!, $sortAsc: Boolean!) {
                movies(page: $page, pageSize: $pageSize, sortBy: $sortBy, sortAsc: $sortAsc) {
                    movies {
                        id
                        title
                        releaseYear
                        rating
                    }
                }
            }
        `,
        { page: 0, pageSize: 9, sortBy: "releaseDate", sortAsc: false }
    );

    return {
        latestMovies: data.movies.movies
    };
};