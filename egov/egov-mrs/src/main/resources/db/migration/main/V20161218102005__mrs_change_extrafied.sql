Alter table egmrs_registration drop locality;
Alter table egmrs_registration add COLUMN locality bigint;
Alter table egmrs_registration add CONSTRAINT fk_egmrs_reg_locality FOREIGN KEY (locality)
      REFERENCES eg_boundary (id);


Alter table egmrs_applicant drop locality;
Alter table egmrs_applicant add COLUMN locality bigint;
Alter table egmrs_applicant add CONSTRAINT fk_egmrs_app_locality FOREIGN KEY (locality)
      REFERENCES eg_boundary (id);

Alter table egmrs_applicant drop nationality;
Alter table egmrs_applicant add COLUMN nationality bigint;
Alter table egmrs_applicant add CONSTRAINT fk_egmrs_app_nationality FOREIGN KEY (nationality)
      REFERENCES eg_nationality (id);