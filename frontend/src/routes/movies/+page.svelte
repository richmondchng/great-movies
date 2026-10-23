<script lang="ts">
  // import type { PageProps } from './$types';
  // import { resolve } from '$app/paths';

  // let { data }: PageProps = $props();



  import { onMount } from 'svelte';
  import { resolve } from '$app/paths';
  import type { MovieListItem, Movies } from '#lib/types/movie.ts';

  const PAGE_SIZE = 10;

  let movies = $state<MovieListItem[]>([]);
  let page = $state(0);
  let hasMore = $state(true);
  let loading = $state(false);
  let error = $state<string | null>(null);

  let sentinel = $state<HTMLDivElement>();

  async function loadNextPage() {
      console.log(`values ${loading} and ${hasMore}`)
      // Prevent concurrent requests and unnecessary requests.
      if (loading || !hasMore) return;

      loading = true;
      error = null;

      try {
          const response = await fetch(
              `/api/movies?page=${page}&size=${PAGE_SIZE}`
          );
          // const response = await fetch(
          //     `/api/movies`
          // );

          if (!response.ok) {
              throw new Error(`HTTP error: ${response.status}`);
          }

          const result: Movies<MovieListItem> = await response.json();

            console.log(`adding ${JSON.stringify(result.movies)}`)
            console.log(`page ${result.page} with total ${result.totalPages}`)
          // Append the next page, keeping existing movies.
          movies = [...movies, ...result.movies];

          page = result.page + 1;
          hasMore = page < result.totalPages;
      } catch (e) {
          error = e instanceof Error
              ? e.message
              : 'Failed to load movies';
      } finally {
          loading = false;
      }
      console.log(`values ${loading} and ${hasMore}`)
  }

  onMount(() => {
      const observer = new IntersectionObserver(
          (entries) => {
              if (entries.some((entry) => entry.isIntersecting)) {
                console.log("Lets load more!")
                  void loadNextPage();
              }
          },
          {
              rootMargin: '300px'
          }
      );

      if (sentinel) {
        observer.observe(sentinel);
      }

      return () => observer.disconnect();
  });
</script>

<svelte:head>
  <title>Movies</title>
</svelte:head>

<h1>Movies</h1>

<main>
<!-- {#if data.movies.length === 0}
  <p>No movies found.</p>
{:else} -->
  <div class="overflow-hidden rounded-lg border border-gray-200">
    {#each movies as movie, i (movie.id)}
      <a href={resolve('/movies/[movieId]', { movieId: String(movie.id) })}
        class={`flex items-center gap-4 p-4 transition-colors hover:bg-blue-100 ${i % 2 === 0 ? 'bg-gray-50' : 'bg-gray-200'}` }
      >
        <!-- Movie poster -->
        {#if movie.coverImageUrl}
          <img
            src={movie.coverImageUrl.url}
            alt={`${movie.title} poster`}
            class="img-border h-28 w-20"
          />
        {:else}
          <div
            role="img"
            aria-label={`No poster available for ${movie.title}`}
            class="question-mark-placeholder h-28 w-20"
          >
            <span class="text-4xl font-light">?</span>
          </div>
        {/if}

        <!-- Movie details -->
        <div class="min-w-0 flex-1">
          <h2>
            {movie.title} ({movie.releaseYear})
          </h2>

          {#if movie.summary}
            <p class="mt-1 line-clamp-2 text-sm text-gray-600">
              {movie.summary}
            </p>
          {/if}
        </div>

        <!-- Rating -->
        <div class="flex shrink-0 items-center gap-1 text-right">
          <span class="text-lg text-yellow-500">★</span>
          <h2>{movie.rating != null ? movie.rating : 'N/A'}</h2>
        </div>
      </a>
    {/each}
  </div>
<!-- {/if} -->

    {#if error}
        <p role="alert">{error}</p>
        <button onclick={loadNextPage}>Retry</button>
    {:else if loading}
        <p aria-live="polite">Loading movies...</p>
    {:else if movies.length > 0}
        <p>You've reached the end of the movies.</p>
    {:else}
        <p>No movies found.</p>
    {/if}
    <div bind:this={sentinel} class="loading-sentinel"></div>
</main>

<style>
  .loading-sentinel {
    height: 1px;
  }
</style>