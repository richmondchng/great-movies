CREATE TABLE person (
    id UUID PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50),
    title VARCHAR(50),
    category VARCHAR(20),
    created_at TIMESTAMP
);