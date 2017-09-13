INSERT INTO eg_user (id, title, salutation, dob, locale, username, password, pwdexpirydate, mobilenumber,
altcontactnumber, emailid, createddate, lastmodifieddate, createdby, lastmodifiedby, active, name, gender,
pan, aadhaarnumber, type, version, guardian, guardianrelation)
VALUES (nextval('seq_eg_user'), NULL, 'MR.', NULL, 'en_IN', 'system', 'NONE', '2010-01-01 00:00:00', NULL, NULL,
NULL, '2010-01-01 00:00:00', '2015-01-01 00:00:00', 1, 1, true, 'System', NULL, NULL, NULL, 'SYSTEM', 0, NULL, NULL);

INSERT INTO eg_systemuser (id) values ((select id from eg_user where username='system'));
