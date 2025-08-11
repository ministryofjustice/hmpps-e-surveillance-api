CREATE TABLE IF NOT EXISTS persons (
    id BIGINT PRIMARY KEY,
    delius_id VARCHAR(100),
    unique_device_wearer_id VARCHAR(100),
    person_id VARCHAR(100),
    given_name VARCHAR(100),
    family_name VARCHAR(100),
    alias VARCHAR(100),
    created_at VARCHAR(100),
    toy BOOLEAN
);

CREATE TABLE IF NOT EXISTS events (
    id SERIAL PRIMARY KEY,
    person_id VARCHAR(255) NOT NULL,
    event_name VARCHAR(255) NOT NULL,
    timestamp VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS notifications (
    id BIGSERIAL PRIMARY KEY,
    person_id VARCHAR(255) NOT NULL,
    violation VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    timestamp TIMESTAMP NOT NULL
);
