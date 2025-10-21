ALTER TABLE prescriptions_tbl
ADD status VARCHAR(15) NOT NULL;

ALTER TABLE prescriptions_tbl
MODIFY status VARCHAR(50) NOT NULL;