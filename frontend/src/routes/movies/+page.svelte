<script lang="ts">
  import type { PageProps } from './$types';
  import { resolve } from '$app/paths';

  let { data }: PageProps = $props();
</script>

<svelte:head>
  <title>Movies</title>
</svelte:head>

<h1>Movies</h1>

{#if data.movies.length === 0}
  <p>No movies found.</p>
{:else}
  <div class="overflow-hidden rounded-lg border border-gray-200">
    {#each data.movies as movie, i (movie.id)}
      <a href={resolve('/movies/[movieId]', { movieId: String(movie.id) })}
        class={`flex items-center gap-4 p-4 transition-colors hover:bg-blue-100 ${i % 2 === 0 ? 'bg-gray-50' : 'bg-gray-200'}` }
      >
        <!-- Movie poster -->
        {#if movie.posterUrl}
          <img
            src={movie.posterUrl}
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
{/if}