CREATE TABLE prescription_charges_tbl(
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    price DOUBLE PRECISION NOT NULL,
    enabled BOOLEAN NOT NULL,
    created_at DATE NOT NULL,
    created_by VARCHAR(36) NOT NULL,
    updated_at DATE NOT NULL,
    updated_by VARCHAR(36) NOT NULL
);