CREATE TABLE payments_tbl(
    id VARCHAR(36) PRIMARY KEY,
    patient_id VARCHAR(36) NOT NULL,
    entity_id VARCHAR(36) NOT NULL,
    amount DOUBLE PRECISION NOT NULL,
    access_code VARCHAR(100),
    paid_at VARCHAR(50),
    currency VARCHAR(10),
    status VARCHAR(20) NOT NULL,
    payment_method VARCHAR(20),
    transaction_id VARCHAR(100),
    reference_number VARCHAR(100),
    authorization_url VARCHAR(200) NOT NULL,
    service_type VARCHAR(25) NOT NULL,
    created_by VARCHAR(36) NOT NULL,
    created_at DATE NOT NULL,
    updated_by VARCHAR(36),
    updated_at DATE
);