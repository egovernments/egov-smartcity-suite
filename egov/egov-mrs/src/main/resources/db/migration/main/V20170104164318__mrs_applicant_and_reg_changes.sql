update  egmrs_document set name ='Electricity Bill' where code='MSEBBILL';

ALTER TABLE egmrs_applicant DROP CONSTRAINT fk_egmrs_app_locality;
 
ALTER TABLE egmrs_applicant ALTER COLUMN locality TYPE character varying(30) USING locality::character varying(30);

update egmrs_document set documentprooftype='ADDRESS_PROOF' WHERE code in ('Aadhar');

ALTER TABLE egmrs_registration ALTER COLUMN placeofmarriage DROP NOT NULL;


Alter table egmrs_applicant drop qualification;

ALTER TABLE egmrs_applicant ADD COLUMN qualification bigint;

Alter table egmrs_applicant add CONSTRAINT fk_egmrs_app_qualification FOREIGN KEY (qualification) REFERENCES eg_qualification (id);