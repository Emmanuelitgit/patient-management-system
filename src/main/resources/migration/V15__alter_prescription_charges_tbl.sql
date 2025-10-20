ALTER TABLE prescription_charges_tbl
MODIFY created_at DATE;

ALTER TABLE prescription_charges_tbl
MODIFY updated_at DATE;

ALTER TABLE prescription_charges_tbl
MODIFY created_by VARCHAR(36);

ALTER TABLE prescription_charges_tbl
MODIFY updated_by VARCHAR(36);