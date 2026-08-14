TRUNCATE TABLE estimate_item, estimate, project, client RESTART IDENTITY CASCADE;

ALTER TABLE client ADD COLUMN user_id BIGINT NOT NULL REFERENCES users(id);