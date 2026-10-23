<script lang="ts">
  import type { PageProps } from './$types';
  import { resolve } from '$app/paths';

  let { data }: PageProps = $props();
</script>

<svelte:head>
  <title>Movies</title>
</svelte:head>

<h1>Movies</h1>
{JSON.stringify(data.movie)}