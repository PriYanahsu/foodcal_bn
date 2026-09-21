CREATE TABLE IF NOT EXISTS weight (
    id         UUID PRIMARY KEY,
    user_id    UUID NOT NULL REFERENCES user_details(id),
    weight_kg  FLOAT8 NOT NULL,
    logged_on  DATE NOT NULL,
    created_at TIMESTAMP NOT NULL
);

CREATE UNIQUE INDEX IF NOT EXISTS weight_one_per_day
    ON weight (user_id, logged_on);

CREATE INDEX IF NOT EXISTS weight_user_on_idx
    ON weight (user_id, logged_on DESC);
