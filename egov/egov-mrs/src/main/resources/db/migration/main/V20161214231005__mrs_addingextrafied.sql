ALTER TABLE egmrs_registration ADD COLUMN venue character varying(30);
ALTER TABLE egmrs_registration ADD COLUMN street character varying(30);
ALTER TABLE egmrs_registration ADD COLUMN locality character varying(30);
ALTER TABLE egmrs_registration ADD COLUMN city character varying(30);

ALTER TABLE egmrs_witness ADD COLUMN relativename character varying(30);
ALTER TABLE egmrs_witness ADD COLUMN witnessRelation character varying(3);
ALTER TABLE egmrs_witness add column applicanttype character varying(10);


ALTER TABLE egmrs_applicant ADD COLUMN parentsname character varying(30);
ALTER TABLE egmrs_applicant ADD COLUMN nationality character varying(30);
ALTER TABLE egmrs_applicant ADD COLUMN qualification character varying(30) ;
ALTER TABLE egmrs_applicant ADD COLUMN street character varying(30);
ALTER TABLE egmrs_applicant ADD COLUMN locality character varying(30);
ALTER TABLE egmrs_applicant ADD COLUMN city character varying(30);

