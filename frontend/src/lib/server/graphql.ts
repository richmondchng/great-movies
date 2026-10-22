import { MOVIES_GRAPHQL_URL } from '$app/env/private'

export async function graphql<T>(
  fetchFn: typeof fetch,
  query: string,
  variables: Record<string, unknown> = {}
): Promise<T> {
  console.log(`query param ${JSON.stringify({ query, variables })}`)

  const response = await fetchFn(MOVIES_GRAPHQL_URL, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({ query, variables })
  });

  if (!response.ok) {
    throw new Error(`GraphQL HTTP error: ${response.status}`);
  }

  const result: {
    data?: T;
    errors?: { message: string }[];
  } = await response.json();

  if (result.errors?.length || result.data === undefined) {
    throw new Error(
      result.errors?.map((e) => e.message).join('; ') ||
        'GraphQL response contains no data'
    );
  }

  return result.data;
}