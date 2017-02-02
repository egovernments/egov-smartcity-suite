ALTER TABLE eglc_legalcase ALTER COLUMN officerincharge TYPE bigint USING officerincharge::bigint;

ALTER TABLE eglc_legalcase 
ADD CONSTRAINT fk_legalcase_position FOREIGN KEY (officerincharge) REFERENCES eg_position (id);
