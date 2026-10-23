<script lang="ts">
  import type { PageProps } from './$types';
  import { resolve } from '$app/paths';

  let { data }: PageProps = $props();
</script>

<svelte:head>
    <title>Great Movies | Home</title>
    <meta name="description" content="Discover the latest movies on Great Movies." />
</svelte:head>

<main class="home-page">
    <div class="section-heading">
        <div>
            <h1>Latest Movies</h1>
            <p>Discover the latest movies and releases.</p>
        </div>
        <a href={resolve("/movies")} class="view-all">View all movies →</a>
    </div>

    <div class="movie-grid">
        {#each data.latestMovies as movie (movie.id)}
            <a href={resolve('/movies/[movieId]', { movieId: String(movie.id) })} class="movie-card interactive-card">
                <div class="poster-container">
                    {#if movie.coverImageUrl}
                        <img
                            src={movie.coverImageUrl.url}
                            alt={`${movie.title} movie poster`}
                            class="movie-poster"
                            loading="lazy"
                        />
                    {:else}
                        <div
                            class="poster-placeholder"
                            role="img"
                            aria-label={`No poster available for ${movie.title}`}
                        >
                            <span aria-hidden="true">?</span>
                        </div>
                    {/if}
                </div>

                <div class="movie-info">
                    <h2>{movie.title}</h2>
                    <p>{movie.releaseYear}</p>
                </div>
            </a>
        {/each}
    </div>
</main>

<style>
/* Home page */
.home-page {
    padding: 2rem;
}

.section-heading {
    display: flex;
    align-items: center;
    justify-content: space-between;
    gap: 1rem;
    margin-bottom: 1.5rem;
}

.section-heading h1 {
    margin: 0;
    color: #0f172a;
    font-size: 2rem;
    font-weight: 700;
}

.section-heading p {
    margin: 0.5rem 0 0;
    color: #64748b;
}

.view-all {
    flex-shrink: 0;
    color: #dc2626;
    font-weight: 600;
}

/* 3 columns x 3 rows when there are 9 movies */
.movie-grid {
    display: grid;
    grid-template-columns: repeat(3, minmax(0, 1fr));
    gap: 2rem 1.5rem;
}

/* Movie card */
.movie-card {
    display: block;
    min-width: 0;
    overflow: hidden;
    color: inherit;
    text-decoration: none;
    background: #ffffff;
    border: 1px solid #e2e8f0;
    border-radius: 0.75rem;
    transition:
        transform 180ms ease,
        box-shadow 180ms ease;
}

.movie-card:hover {
    transform: translateY(-4px);
    box-shadow: 0 10px 24px rgb(15 23 42 / 10%);
    text-decoration: none;
}

.poster-container {
    width: 100%;
    aspect-ratio: 2 / 3;
    overflow: hidden;
    background: #e2e8f0;
}

.movie-card:hover .movie-poster {
    transform: scale(1.04);
}

.movie-info {
    padding: 0.9rem 1rem 1rem;
}

.movie-info h2 {
    margin: 0;
    overflow: hidden;
    color: #0f172a;
    font-size: 1rem;
    font-weight: 600;
    line-height: 1.4;
    text-overflow: ellipsis;
    white-space: nowrap;
}

.movie-info p {
    margin: 0.4rem 0 0;
    color: #64748b;
    font-size: 0.9rem;
}

/* Tablet */
@media (max-width: 768px) {
    .home-page {
        padding: 1.5rem;
    }

    .movie-grid {
        grid-template-columns: repeat(2, minmax(0, 1fr));
        gap: 1.5rem 1rem;
    }
}

/* Mobile */
@media (max-width: 480px) {
    .home-page {
        padding: 1rem;
    }

    .section-heading {
        align-items: flex-start;
    }

    .section-heading h1 {
        font-size: 1.6rem;
    }

    .section-heading p {
        font-size: 0.9rem;
    }

    .view-all {
        font-size: 0.85rem;
    }

    .movie-grid {
        grid-template-columns: 1fr;
        gap: 1rem;
    }

    .movie-card {
        display: grid;
        grid-template-columns: 100px minmax(0, 1fr);
    }

    .poster-container {
        aspect-ratio: 2 / 3;
    }

    .movie-info {
        align-self: center;
    }

    .movie-info h2 {
        white-space: normal;
    }
}
</style>