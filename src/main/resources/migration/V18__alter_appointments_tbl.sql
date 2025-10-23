ALTER TABLE appointments_tbl
MODIFY remarks VARCHAR(255);

ALTER TABLE appointments_tbl
ADD COLUMN appointment_charge_id VARCHAR(36);