CREATE TABLE labs_tbl(
    id VARCHAR(36) PRIMARY KEY NOT NULL,
    patient_id VARCHAR(36) NOT NULL,
    doctor_id VARCHAR(36) NOT NULL,
    test_name VARCHAR(100) NOT NULL,
    status VARCHAR(50) NOT NULL,
    result VARCHAR(1000),
    created_at DATE,
    created_by VARCHAR(36),
    updated_at DATE,
    updated_by VARCHAR(36)
);