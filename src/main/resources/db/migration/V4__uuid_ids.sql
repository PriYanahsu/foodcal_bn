DROP TABLE IF EXISTS food_log CASCADE;
DROP TABLE IF EXISTS notification CASCADE;
DROP TABLE IF EXISTS subscription CASCADE;
DROP TABLE IF EXISTS fitness_details CASCADE;
DROP TABLE IF EXISTS user_details CASCADE;

CREATE TABLE user_details (
    id         UUID PRIMARY KEY,
    user_name  VARCHAR(255),
    full_name  VARCHAR(255),
    email      VARCHAR(255),
    gender     VARCHAR(255),
    avatar_url VARCHAR(255),
    role       VARCHAR(255),
    created_at DATE,
    updated_at DATE
);

CREATE TABLE fitness_details (
    id                     UUID PRIMARY KEY,
    user_id                UUID NOT NULL UNIQUE REFERENCES user_details(id),
    age                    INTEGER NOT NULL,
    height                 NUMERIC(38, 2) NOT NULL,
    weight                 NUMERIC(38, 2) NOT NULL,
    activity_level         VARCHAR(255) NOT NULL,
    target_weight_kg       NUMERIC(38, 2) NOT NULL,
    target_date            DATE NOT NULL,
    daily_calorie_target   INTEGER NOT NULL,
    daily_protein_target_g NUMERIC(38, 2) NOT NULL,
    daily_carbs_target_g   NUMERIC(38, 2) NOT NULL,
    daily_fat_target_g     NUMERIC(38, 2) NOT NULL,
    ai_coach_advice        TEXT NOT NULL
);

CREATE TABLE food_log (
    id              UUID PRIMARY KEY,
    user_id         UUID NOT NULL REFERENCES user_details(id),
    food_name       VARCHAR(255),
    calories        FLOAT8,
    protein_g       FLOAT8,
    fat_g           FLOAT8,
    carbohydrate_g  FLOAT8,
    ai_confidence   FLOAT8,
    image_path      VARCHAR(255),
    meal_type       VARCHAR(255),
    is_manual       BOOLEAN,
    created_at      TIMESTAMP
);

CREATE TABLE notification (
    id         UUID PRIMARY KEY,
    user_id    UUID NOT NULL REFERENCES user_details(id),
    title      VARCHAR(255),
    message    VARCHAR(255),
    type       VARCHAR(255),
    is_read    BOOLEAN,
    meta_data  VARCHAR(255),
    created_at TIMESTAMP
);

CREATE TABLE subscription (
    id                       UUID PRIMARY KEY,
    user_id                  UUID NOT NULL REFERENCES user_details(id),
    plan                     VARCHAR(255),
    status                   VARCHAR(255),
    start_date               DATE,
    end_date                 DATE,
    payment_provider         VARCHAR(255),
    payment_subscription_id  VARCHAR(255),
    created_at               TIMESTAMP,
    updated_at               TIMESTAMP
);
