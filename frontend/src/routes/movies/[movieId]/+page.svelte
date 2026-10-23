<script lang="ts">
  import './movie-details.css';
  import type { PageProps } from './$types';
  import type { MovieDetailsItem } from '#lib/types/movie.ts';

  let { data }: PageProps = $props();

  let movie : MovieDetailsItem = $derived(data.movie);
  console.log(`Movie details ${JSON.stringify(movie)}`)

  let selectedFiles: FileList | null = null;

  function handleFileChange(event: Event) {
    const input = event.currentTarget as HTMLInputElement;
    selectedFiles = input.files;
  }

  function handleUpload() {
    if (!selectedFiles?.length) return;

    // TODO: Upload selectedFiles to your file-management backend.
    // For example, build FormData and POST it to your upload API.
    console.log("Files selected:", Array.from(selectedFiles));
  }
</script>

<svelte:head>
  <title>{movie.title} ({movie.releaseYear ?? "Year unknown"})</title>
  <meta
    name="description"
    content={movie.summary ?? `Details for ${movie.title}`}
  />
</svelte:head>

<main class="movie-details">
  <!-- Movie title -->
  <header class="movie-header">
    <h1>{movie.title}</h1>
    <span class="release-year">
      {movie.releaseYear ?? "Release year unknown"}
    </span>
  </header>

  <!-- Poster and movie images -->
  <section class="media-panel" aria-label="Movie images">
    <div class="poster-container">
      {#if movie.coverImageUrl}
        <img
          class="poster-image"
          src={movie.coverImageUrl.url}
          alt={`Poster for ${movie.title}`}
        />
      {:else}
        <div
          class="image-placeholder poster-placeholder"
          role="img"
          aria-label="Movie poster unavailable"
        >
          <span aria-hidden="true">?</span>
        </div>
      {/if}
    </div>

    <div class="gallery-container">
      <h2>Movie Images</h2>

      <div class="image-gallery">
        {#each movie.images as image (image.id)}
          <div class="gallery-item">
            {#if image.url}
              <img
                src={image.url}
                alt={`${movie.title} scene`}
                loading="lazy"
              />
            {:else}
              <div
                class="image-placeholder"
                role="img"
                aria-label="Movie image unavailable"
              >
                <span aria-hidden="true">?</span>
              </div>
            {/if}
          </div>
        {/each}

        {#if movie.images.length === 0}
          <p class="empty-message">No additional images available.</p>
        {/if}
      </div>
    </div>
  </section>

   <!-- Upload additional images -->
  <div class="upload-section">
    <label for="movie-images" class="upload-button">
      + Add Movie Images
    </label>

    <input
      id="movie-images"
      class="file-input"
      type="file"
      accept="image/*"
      multiple
     // onchange={handleFileChange}
    />

    {#if selectedFiles?.length}
      <span class="selected-files">
        {selectedFiles.length} file(s) selected
      </span>

      <button class="upload-button">
        Upload
      </button>
    {/if}
  </div>

    <!-- Summary and rating -->
  <section class="summary-section">
    <div class="summary-content">
      <h2>Overview</h2>
      <p>{movie.summary || "No summary available."}</p>
    </div>

    <aside class="rating-panel" aria-label="Movie rating">
      <span class="rating-label">Rating</span>
      <strong class="rating-value">{movie.rating}</strong>
      <span class="rating-scale">/ 10</span>
    </aside>
  </section>

   <!-- Crew -->
  <section class="people-section">
    <div class="section-header">
      <h2>Crew</h2>
      <span class="item-count">{Math.min(movie.crew.length, 5)} members</span>
    </div>

    <div class="people-grid crew-grid">
      {#each movie.crew.slice(0, 5) as person (person.personId)}
        <article class="person-card">
          {#if person.coverImageUrl}
            <img
              class="person-photo"
              src={person.coverImageUrl.url}
              alt={person.firstName + " " + person.lastName}
              loading="lazy"
            />
          {:else}
            <div
              class="image-placeholder person-photo"
              role="img"
              aria-label={`Photo unavailable for ${person.firstName + " " + person.lastName}`}
            >
              <span aria-hidden="true">?</span>
            </div>
          {/if}

          <h3>{person.firstName + " " + person.lastName}</h3>
          <p class="person-subtitle">{person.jobName}</p>
        </article>
      {/each}

      {#if movie.crew.length === 0}
        <p class="empty-message">No crew information available.</p>
      {/if}
    </div>
  </section>

  <!-- Cast -->
  <section class="people-section">
    <div class="section-header">
      <h2>Cast</h2>
      <span class="item-count">{Math.min(movie.cast.length, 20)} actors</span>
    </div>

    <div class="people-grid cast-grid">
      {#each movie.cast.slice(0, 20) as actor (actor.personId)}
        <article class="person-card">
          {#if actor.coverImageUrl}
            <img
              class="person-photo"
              src={actor.coverImageUrl.url}
              alt={actor.firstName + " " + actor.lastName}
              loading="lazy"
            />
          {:else}
            <div
              class="image-placeholder person-photo"
              role="img"
              aria-label={`Photo unavailable for ${actor.firstName + " " + actor.lastName}`}
            >
              <span aria-hidden="true">?</span>
            </div>
          {/if}

          <h3>{actor.firstName + " " + actor.lastName}</h3>
          <p class="person-subtitle">
            {actor.characterName || "Character unknown"}
          </p>
        </article>
      {/each}

      {#if movie.cast.length === 0}
        <p class="empty-message">No cast information available.</p>
      {/if}
    </div>
  </section>
</main>
