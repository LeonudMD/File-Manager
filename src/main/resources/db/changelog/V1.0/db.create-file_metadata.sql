CREATE TABLE file_metadata (
                               id BIGSERIAL PRIMARY KEY,
                               filename VARCHAR(255) NOT NULL,
                               content_type VARCHAR(255) NOT NULL,
                               file_size BIGINT NOT NULL,
                               created_time TIMESTAMP NOT NULL
);
