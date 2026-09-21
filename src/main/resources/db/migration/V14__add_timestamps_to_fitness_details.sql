ALTER TABLE fitness_details
    ADD COLUMN IF NOT EXISTS created_at TIMESTAMP,
    ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP;

-- Existing rows: fall back to when the user signed up, which is never later than their first weigh-in.
UPDATE fitness_details f
SET created_at = COALESCE(u.created_at, NOW()),
    updated_at = COALESCE(u.updated_at, u.created_at, NOW())
FROM user_details u
WHERE f.user_id = u.id
  AND f.created_at IS NULL;
