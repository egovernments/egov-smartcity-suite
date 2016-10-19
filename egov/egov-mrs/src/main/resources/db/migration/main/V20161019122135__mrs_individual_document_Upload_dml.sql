
update egmrs_document set name= '100/- court fee stamps ( under provision court fee act 1859) affixed' where code='CF_STAMP';

INSERT INTO egmrs_document(ID, NAME,type, CODE, INDIVIDUAL, VERSION, CREATEDBY, CREATEDDATE, LASTMODIFIEDBY, LASTMODIFIEDDATE)
VALUES(nextval('seq_egmrs_document'), 'School Leaving Certificate','REGISTRATION', 'SLC',true, 1, 1, now(), 1,  now());
INSERT INTO egmrs_document(ID, NAME,type, CODE, INDIVIDUAL, VERSION, CREATEDBY, CREATEDDATE, LASTMODIFIEDBY, LASTMODIFIEDDATE)
VALUES(nextval('seq_egmrs_document'), 'Birth Certificate','REGISTRATION', 'BC',true, 1, 1, now(), 1,  now());
INSERT INTO egmrs_document(ID, NAME,type, CODE, INDIVIDUAL, VERSION, CREATEDBY, CREATEDDATE, LASTMODIFIEDBY, LASTMODIFIEDDATE)
VALUES(nextval('seq_egmrs_document'), 'Divorce Certificate where applicable','REGISTRATION', 'DCA',true, 1, 1, now(), 1,  now());
INSERT INTO egmrs_document(ID, NAME,type, CODE, INDIVIDUAL, VERSION, CREATEDBY, CREATEDDATE, LASTMODIFIEDBY, LASTMODIFIEDDATE)
VALUES(nextval('seq_egmrs_document'), 'Death Certificate of deceased spouse where applicable','REGISTRATION', 'DCSWA',true, 1, 1, now(), 1,  now());

INSERT INTO egmrs_document(ID, NAME,type, CODE, INDIVIDUAL, VERSION, CREATEDBY, CREATEDDATE, LASTMODIFIEDBY, LASTMODIFIEDDATE)
VALUES(nextval('seq_egmrs_document'), 'Passport','REGISTRATION', 'Passport',true, 1, 1, now(), 1,  now());

INSERT INTO egmrs_document(ID, NAME,type, CODE, INDIVIDUAL, VERSION, CREATEDBY, CREATEDDATE, LASTMODIFIEDBY, LASTMODIFIEDDATE)
VALUES(nextval('seq_egmrs_document'), 'Ration Card','REGISTRATION', 'RationCard',true, 1, 1, now(), 1,  now());
INSERT INTO egmrs_document(ID, NAME,type, CODE, INDIVIDUAL, VERSION, CREATEDBY, CREATEDDATE, LASTMODIFIEDBY, LASTMODIFIEDDATE)
VALUES(nextval('seq_egmrs_document'), 'M. S. E. B Bill','REGISTRATION', 'MSEBBILL',true, 1, 1, now(), 1,  now());
INSERT INTO egmrs_document(ID, NAME,type, CODE, INDIVIDUAL, VERSION, CREATEDBY, CREATEDDATE, LASTMODIFIEDBY, LASTMODIFIEDDATE)
VALUES(nextval('seq_egmrs_document'), 'Telephone Bill','REGISTRATION', 'TelephoneBill ',true, 1, 1, now(), 1,  now());
INSERT INTO egmrs_document(ID, NAME,type, CODE, INDIVIDUAL, VERSION, CREATEDBY, CREATEDDATE, LASTMODIFIEDBY, LASTMODIFIEDDATE)
VALUES(nextval('seq_egmrs_document'), 'Others','REGISTRATION', 'Others ',true, 1, 1, now(), 1,  now());
