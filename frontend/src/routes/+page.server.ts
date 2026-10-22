import { getLatestMovies } from '#lib/server/movies.ts';
import type { PageServerLoad } from './$types';

export const load : PageServerLoad = async ({ fetch }) => {
    console.log("client load")
    const data = await getLatestMovies(fetch);
    return {
        latestMovies: data.movies
    };
};