import type { ImageItem } from '#lib/types/movie.ts';
import { FILE_MGT_URL } from '$app/env/private'

export async function uploadImage(
    fetchFn: typeof fetch,
    formData: FormData
): Promise<ImageItem> {

//   const formData = await request.formData();

  const response = await fetchFn(
    `${FILE_MGT_URL}/api/v1/upload`,
    {
      method: 'POST',
      body: formData
    }
  );

  const body: ImageItem = await response.json();

  return body;
}