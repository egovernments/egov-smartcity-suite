ALTER TABLE egmrs_registration ALTER COLUMN placeofmarriage TYPE character varying(100);
ALTER TABLE egmrs_registration ALTER COLUMN street TYPE character varying(100);

ALTER TABLE egmrs_applicant ALTER COLUMN locality TYPE character varying (100);
ALTER TABLE egmrs_applicant ALTER COLUMN street TYPE character varying(100); 
ALTER TABLE egmrs_applicant ALTER COLUMN parentsname TYPE character varying(70); 

ALTER TABLE egmrs_witness ALTER COLUMN relativename TYPE character varying(70); 