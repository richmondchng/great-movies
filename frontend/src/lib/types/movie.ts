export interface Movie {
    id: number;
    title: string;
    summary: string;
    posterUrl: string | null;
    releaseYear: number | null;
    rating: string | null;
}

export interface Movies {
    movies: Movie[]
}

export interface MoviesResponse {
    movies: Movies;
    page: number;
    pageSize: number;
    numberOfElements: number;
    totalElements: number;
    totalPages: number;
}