import type { PageServerLoad } from './$types';
// import { getMovies } from '#lib/server/movies.ts';

export const load: PageServerLoad = async ({ fetch }) => {
    // const data = await getMovies(fetch, 0, 10)
    return {
        movies: [], //data.movies,
        page: 1, //data.page,
        totalPages: 1 //data.totalPages
    };
};