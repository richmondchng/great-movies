import { defineEnvVars } from '@sveltejs/kit/env';

export const variables = defineEnvVars({
  MOVIES_GRAPHQL_URL: {},
  FILE_MGT_URL: {}
});