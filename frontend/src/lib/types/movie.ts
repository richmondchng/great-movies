export interface MovieListItem {
    id: number;
    title: string;
    summary: string;
    posterUrl: string | null;
    releaseYear: number | null;
    rating: string | null;
}

export interface LatestMovieItem {
    id: number;
    title: string;
    posterUrl: string | null;
    releaseYear: number | null;
}

export interface Movies<T> {
    movies: T[]
    page: number;
    pageSize: number;
    numberOfElements: number;
    totalElements: number;
    totalPages: number;
}

export interface MoviesResponse<T> {
    movies: Movies<T>;
}