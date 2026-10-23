CREATE TABLE movie (
    id UUID PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    summary VARCHAR(500),
    synopsis TEXT,
    release_year INT,
    release_date DATE,
    rating DECIMAL,
    created_at TIMESTAMP
);
