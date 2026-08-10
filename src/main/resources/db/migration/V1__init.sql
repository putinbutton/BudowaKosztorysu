CREATE TABLE client (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    phone_number VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL
);

CREATE TABLE project (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    creation_date DATE NOT NULL,
    client_id BIGINT NOT NULL REFERENCES client(id)
);

CREATE TABLE estimate (
    id BIGSERIAL PRIMARY KEY,
    creation_date DATE NOT NULL,
    project_id BIGINT UNIQUE REFERENCES project(id)
);

CREATE TABLE estimate_item (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    item_type VARCHAR(50) NOT NULL,
    unit VARCHAR(50) NOT NULL,
    quantity NUMERIC(38,2) NOT NULL,
    price_per_unit NUMERIC(38,2) NOT NULL,
    price_per_hour NUMERIC(38,2),
    estimate_id BIGINT REFERENCES estimate(id)
);