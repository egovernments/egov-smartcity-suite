alter table eglc_legalcase drop column officerincharge;

alter table eglc_legalcase add column officerincharge bigint ;

ALTER TABLE eglc_legalcase ADD CONSTRAINT fk_legalcase_officerposition FOREIGN KEY (officerincharge) REFERENCES eg_position (id);
