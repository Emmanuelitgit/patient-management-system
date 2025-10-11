CREATE TABLE appointments_tbl(
    id VARCHAR(36) PRIMARY KEY,
    doctor_id VARCHAR(36) NOT NULL,
    patient_id VARCHAR(36) NOT NULL,
    capacity INTEGER,
    status VARCHAR(50) NOT NULL,
    remarks VARCHAR(255) NOT NULL,
    date DATE NOT NULL,
    time TIME NOT NULL,
    created_by VARCHAR(36) NOT NULL,
    created_at DATE NOT NULL,
    updated_by VARCHAR(36),
    updated_at DATE
)