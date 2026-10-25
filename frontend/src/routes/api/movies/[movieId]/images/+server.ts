
import type { RequestHandler } from '@sveltejs/kit';
// import { getMovies } from '#lib/server/movies.ts';
import { uploadImage } from '#lib/server/filemgt.ts';

export const POST: RequestHandler = async ({
  params,
  request,
  fetch
}) => {
    const formData = await request.formData();
    
    const resourceId = params.movieId!;
    formData.append('id', resourceId)

    const data = await uploadImage(fetch, formData)
    return Response.json(data)
};
