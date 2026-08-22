CREATE SEQUENCE revinfo_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE revinfo (
    rev INTEGER PRIMARY KEY,
    revtstmp BIGINT
);

CREATE TABLE client_aud (
    id BIGINT NOT NULL,
    rev INTEGER NOT NULL REFERENCES revinfo(rev),
    revtype SMALLINT,
    name VARCHAR(255),
    email VARCHAR(255),
    phone_number VARCHAR(255),
    user_id BIGINT,
    PRIMARY KEY (rev, id)
);

CREATE TABLE project_aud (
    id BIGINT NOT NULL,
    rev INTEGER NOT NULL REFERENCES revinfo(rev),
    revtype SMALLINT,
    creation_date DATE,
    name VARCHAR(255),
    client_id BIGINT,
    PRIMARY KEY (rev, id)
);

CREATE TABLE estimate_aud (
    id BIGINT NOT NULL,
    rev INTEGER NOT NULL REFERENCES revinfo(rev),
    revtype SMALLINT,
    creation_date DATE,
    project_id BIGINT,
    PRIMARY KEY (rev, id)
);

CREATE TABLE estimate_item_aud (
    id BIGINT NOT NULL,
    rev INTEGER NOT NULL REFERENCES revinfo(rev),
    revtype SMALLINT,
    item_type VARCHAR(50),
    name VARCHAR(255),
    price_per_hour NUMERIC(38,2),
    price_per_unit NUMERIC(38,2),
    quantity NUMERIC(38,2),
    unit VARCHAR(50),
    estimate_id BIGINT,
    PRIMARY KEY (rev, id)
);

CREATE TABLE users_aud (
    id BIGINT NOT NULL,
    rev INTEGER NOT NULL REFERENCES revinfo(rev),
    revtype SMALLINT,
    address VARCHAR(255),
    company_name VARCHAR(255),
    email VARCHAR(255),
    failed_login_attempts INTEGER,
    locked_until TIMESTAMP(6),
    nip VARCHAR(255),
    password VARCHAR(255),
    phone VARCHAR(255),
    username VARCHAR(255),
    PRIMARY KEY (rev, id)
);