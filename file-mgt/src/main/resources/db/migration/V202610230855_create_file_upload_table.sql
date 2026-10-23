CREATE TABLE file_metadata (
    id UUID PRIMARY KEY,
    linked_id UUID NOT NULL,
    category VARCHAR(50) NOT NULL DEFAULT 'IMAGE',
    original_file_name VARCHAR(100),
    mime_type VARCHAR(50),
    file_size BIGINT,
    file_path VARCHAR(255),
    file_name VARCHAR(100),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(50),
);

CREATE INDEX idx_file_metadata_linked_id
    ON file_metadata (linked_id);

CREATE INDEX idx_file_metadata_file_name
    ON file_metadata (file_name);