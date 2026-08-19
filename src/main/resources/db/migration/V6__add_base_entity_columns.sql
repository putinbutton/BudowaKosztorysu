ALTER TABLE client ADD COLUMN uuid UUID;
ALTER TABLE client ADD COLUMN version_id INTEGER;

ALTER TABLE project ADD COLUMN uuid UUID;
ALTER TABLE project ADD COLUMN version_id INTEGER;

ALTER TABLE estimate ADD COLUMN uuid UUID;
ALTER TABLE estimate ADD COLUMN version_id INTEGER;

ALTER TABLE estimate_item ADD COLUMN uuid UUID;
ALTER TABLE estimate_item ADD COLUMN version_id INTEGER;

ALTER TABLE users ADD COLUMN uuid UUID;
ALTER TABLE users ADD COLUMN version_id INTEGER;


UPDATE client SET uuid = gen_random_uuid(), version_id = 0 WHERE uuid IS NULL;
UPDATE project SET uuid = gen_random_uuid(), version_id = 0 WHERE uuid IS NULL;
UPDATE estimate SET uuid = gen_random_uuid(), version_id = 0 WHERE uuid IS NULL;
UPDATE estimate_item SET uuid = gen_random_uuid(), version_id = 0 WHERE uuid IS NULL;
UPDATE users SET uuid = gen_random_uuid(), version_id = 0 WHERE uuid IS NULL;


ALTER TABLE client ALTER COLUMN uuid SET NOT NULL;
ALTER TABLE client ADD CONSTRAINT uk_client_uuid UNIQUE (uuid);
ALTER TABLE client ALTER COLUMN version_id SET NOT NULL;

ALTER TABLE project ALTER COLUMN uuid SET NOT NULL;
ALTER TABLE project ADD CONSTRAINT uk_project_uuid UNIQUE (uuid);
ALTER TABLE project ALTER COLUMN version_id SET NOT NULL;

ALTER TABLE estimate ALTER COLUMN uuid SET NOT NULL;
ALTER TABLE estimate ADD CONSTRAINT uk_estimate_uuid UNIQUE (uuid);
ALTER TABLE estimate ALTER COLUMN version_id SET NOT NULL;

ALTER TABLE estimate_item ALTER COLUMN uuid SET NOT NULL;
ALTER TABLE estimate_item ADD CONSTRAINT uk_estimate_item_uuid UNIQUE (uuid);
ALTER TABLE estimate_item ALTER COLUMN version_id SET NOT NULL;

ALTER TABLE users ALTER COLUMN uuid SET NOT NULL;
ALTER TABLE users ADD CONSTRAINT uk_users_uuid UNIQUE (uuid);
ALTER TABLE users ALTER COLUMN version_id SET NOT NULL;