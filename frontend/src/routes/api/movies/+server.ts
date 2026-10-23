
import type { RequestHandler } from '@sveltejs/kit';
import { getMovies } from '#lib/server/movies.ts';

export const GET: RequestHandler = async ({ fetch, url }) => {
    console.log("Getting more movies")
    const pageParam = Number(url.searchParams.get('page') ?? 0);
    const sizeParam = Number(url.searchParams.get('size') ?? 10);

    const data = await getMovies(fetch, pageParam, sizeParam)
    return Response.json(data)
};