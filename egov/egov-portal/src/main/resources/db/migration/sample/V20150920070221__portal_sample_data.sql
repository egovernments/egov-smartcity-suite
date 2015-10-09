
INSERT INTO eg_user (id, title, salutation, dob, locale, username, password, pwdexpirydate, mobilenumber, altcontactnumber, emailid, createddate, lastmodifieddate, createdby, lastmodifiedby, active, name, gender, pan, aadhaarnumber, type, version, guardian, guardianrelation) VALUES (67, NULL, NULL, NULL, 'en_IN', '9999999999', '$2a$10$uheIOutTnD33x7CDqac1zOL8DMiuz7mWplToPgcf7oxAI9OzRKxmK', '2020-12-31 00:00:00', NULL, NULL, NULL, '2010-01-01 00:00:00', '2015-01-01 00:00:00', 1, 1, true, '999999999', NULL, NULL, NULL, 'CITIZEN', 0, NULL, NULL);

INSERT INTO eg_user (id, title, salutation, dob, locale, username, password, pwdexpirydate, mobilenumber, altcontactnumber, emailid, createddate, lastmodifieddate, createdby, lastmodifiedby, active, name, gender, pan, aadhaarnumber, type, version, guardian, guardianrelation) VALUES (69, NULL, 'Mr.', NULL, NULL, '8888888888', '$2a$10$uheIOutTnD33x7CDqac1zOL8DMiuz7mWplToPgcf7oxAI9OzRKxmK', '2099-12-31 00:00:00', '8888888888', NULL, 'sudarshan@test.com', '2015-08-28 10:45:17.00206', '2015-08-28 10:45:17.00206', 1, 1, true, 'sudarshan', 1, NULL, '123123123', 'CITIZEN', 0, NULL, NULL);


-----------------START-------------------
INSERT INTO egp_citizen (id, activationcode, version) VALUES (67, 'XXX', 0);
INSERT INTO egp_citizen (id, activationcode, version) VALUES (69, NULL, 0);

---------------------END-----------------------------
