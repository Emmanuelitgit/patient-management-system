CREATE TABLE prescriptions_tbl(
    id VARCHAR PRIMARY KEY,
    appointment_id VARCHAR NOT NULL,
    patient_id VARCHAR NOT NULL,
    doctor_id VARCHAR NOT NULL,
    medication VARCHAR(100) NOT NULL,
    instructions VARCHAR(1000) NOT NULL,
    dosage FLOAT NOT NULL,
    created_at DATE NOT NULL,
    created_by VARCHAR,
    updated_at DATE,
    updated_by VARCHAR
)