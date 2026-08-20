ALTER TABLE users ADD COLUMN failed_login_attempts INTEGER;
ALTER TABLE users ADD COLUMN locked_until TIMESTAMP;

UPDATE users SET failed_login_attempts = 0 WHERE failed_login_attempts IS NULL;

ALTER TABLE users ALTER COLUMN failed_login_attempts SET NOT NULL;
