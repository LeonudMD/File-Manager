ALTER TABLE file_metadata ADD COLUMN download_link VARCHAR(255);
ALTER TABLE file_metadata ADD COLUMN encrypted_file VARCHAR(255);
ALTER TABLE file_metadata ADD COLUMN one_time_link BOOLEAN DEFAULT FALSE;
ALTER TABLE file_metadata ADD COLUMN expiration_time TIMESTAMP NOT NULL DEFAULT (CURRENT_TIMESTAMP + INTERVAL '1 day');

-- Обновление существующих записей с новыми значениями
UPDATE file_metadata
SET download_link = 'http://localhost:8080/v1/files/download/PostMan_Test.jpg', encrypted_file='111111'
WHERE filename = 'PostMan_Test.jpg';
