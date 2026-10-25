export interface MovieDetailsItem {
    id: string;
    title: string;
    summary: string;
    coverImageUrl: ImageItem | null;
    images: ImageItem[];
    releaseYear: number | null;
    releaseDate: string | null;
    rating: string | null;
    cast: CastDetailItem[];
    crew: CrewDetailItem[];
}

export interface ImageItem {
    id: string;
    url: string;
}

export interface CastDetailItem {
    personId: string;
    firstName: string;
    lastName: string | null;
    characterName: string;
    coverImageUrl: ImageItem | null;
}

export interface CrewDetailItem {
    personId: string;
    firstName: string;
    lastName: string | null;
    jobName: string;
    coverImageUrl: ImageItem | null;
}

export interface MovieListItem {
    id: string;
    title: string;
    summary: string;
    coverImageUrl: ImageItem | null;
    releaseYear: number | null;
    rating: string | null;
}

export interface LatestMovieItem {
    id: string;
    title: string;
    coverImageUrl: ImageItem | null;
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

export interface MovieDetailResponse {
    movie: MovieDetailsItem
}