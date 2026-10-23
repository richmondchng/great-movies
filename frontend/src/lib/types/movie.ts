export interface MovieDetailsItem {
    id: number;
    title: string;
    summary: string;
    posterUrl: string | null;
    releaseYear: number | null;
    releaseDate: string | null;
    rating: string | null;
    cast: CastDetailItem[];
    crew: CrewDetailItem[];
}

export interface CastDetailItem {
    personId: number;
    firstName: string;
    lastName: string | null;
    characterName: string;
    photoUrl: string | null;
}

export interface CrewDetailItem {
    personId: number;
    firstName: string;
    lastName: string | null;
    jobName: string;
    photoUrl: string | null;
}

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