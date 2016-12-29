
---------------------------egmrs_identityproof---------------------------------------

Alter table egmrs_identityproof add column aadhar boolean;
Alter table egmrs_identityproof add column notaryaffidavit boolean;

---------------------------egmrs_document---------------------------------------

INSERT INTO egmrs_document(ID, NAME,type, CODE, INDIVIDUAL, VERSION, CREATEDBY, CREATEDDATE, LASTMODIFIEDBY, LASTMODIFIEDDATE)
VALUES(nextval('seq_egmrs_document'), 'Aadhar','REGISTRATION', 'Aadhar',true, 1, 1, now(), 1,  now());

INSERT INTO egmrs_document(ID, NAME,type, CODE, INDIVIDUAL, VERSION, CREATEDBY, CREATEDDATE, LASTMODIFIEDBY, LASTMODIFIEDDATE)
VALUES(nextval('seq_egmrs_document'), 'Notary Affidavit','REGISTRATION', 'NotaryAffidavit',true, 1, 1, now(), 1,  now());




