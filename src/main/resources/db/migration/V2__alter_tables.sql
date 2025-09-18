ALTER TABLE notifications
ADD COLUMN person_name VARCHAR(255),
ADD COLUMN search_vector tsvector
    GENERATED ALWAYS AS (
        to_tsvector('english', coalesce(person_name, ''))
    ) STORED;
CREATE INDEX idx_notifications_search
    ON notifications USING GIN (search_vector);

ALTER TABLE persons
ADD COLUMN search_vector tsvector
    GENERATED ALWAYS AS (
        to_tsvector('english',
            coalesce(given_name,'') || ' ' ||
            coalesce(family_name,'')
        )
    ) STORED;
CREATE INDEX idx_persons_search
    ON persons USING gin(search_vector);