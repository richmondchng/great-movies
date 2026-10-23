CREATE TABLE job (
    id UUID PRIMARY KEY,
    movie_id UUID NOT NULL REFERENCES movie(id),
    person_id UUID NOT NULL REFERENCES person(id),
    sequence INT NOT NULL DEFAULT 99,
    job_type VARCHAR(10) DEFAULT 'CREW'
);

CREATE TABLE job_cast (
    job_id UUID PRIMARY KEY REFERENCES job(id),
    character_name VARCHAR(100) NOT NULL
);

CREATE TABLE job_crew (
    job_id UUID PRIMARY KEY REFERENCES job(id),
    job_name VARCHAR(100) NOT NULL
);
