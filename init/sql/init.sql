CREATE TABLE IF NOT EXISTS pop_users (
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
