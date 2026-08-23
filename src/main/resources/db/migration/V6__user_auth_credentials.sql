ALTER TABLE user_details
    ADD COLUMN password_hash VARCHAR(255);

UPDATE user_details
SET password_hash = ''
WHERE password_hash IS NULL;

ALTER TABLE user_details
    ALTER COLUMN password_hash SET NOT NULL,
    ALTER COLUMN email SET NOT NULL,
    ALTER COLUMN user_name SET NOT NULL;

CREATE UNIQUE INDEX uk_user_details_email ON user_details (LOWER(email));
CREATE UNIQUE INDEX uk_user_details_user_name ON user_details (LOWER(user_name));
