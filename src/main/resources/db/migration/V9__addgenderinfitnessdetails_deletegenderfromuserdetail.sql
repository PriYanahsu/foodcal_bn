ALTER TABLE fitness_details
    ADD COLUMN gender VARCHAR(255);

ALTER TABLE user_details
    DROP COLUMN IF EXISTS gender;