import { getMovie } from '#lib/server/movies.ts';
import type { PageServerLoad } from './$types';

export const load : PageServerLoad = async ({ params, fetch }) => {
    const movieId = params.movieId;
    const data = await getMovie(fetch, movieId );
    return {
        movie: data
    };
};