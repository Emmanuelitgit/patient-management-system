CREATE TABLE prescriptions_tbl(
    id VARCHAR(36) PRIMARY KEY,
    appointment_id VARCHAR(36) NOT NULL,
    patient_id VARCHAR(36) NOT NULL,
    doctor_id VARCHAR(36) NOT NULL,
    medication VARCHAR(100) NOT NULL,
    instructions VARCHAR(1000) NOT NULL,
    dosage FLOAT NOT NULL,
    created_at DATE NOT NULL,
    created_by VARCHAR(36),
    updated_at DATE,
    updated_by VARCHAR(36)
);