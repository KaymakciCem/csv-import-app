
create schema if not exists ted;

CREATE TABLE IF NOT EXISTS ted.ted_talk (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    event_date DATE NOT NULL,
    views BIGINT NOT NULL,
    likes BIGINT NOT NULL,
    link VARCHAR(255),
    created_by VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);