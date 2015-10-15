--------------------- User Role Mapping for Property Administrator --------------------------

INSERT INTO eg_userrole  (userid,roleid) VALUES ((SELECT id FROM eg_user WHERE username='satyam'),(SELECT id FROM eg_role where name ='Property Administrator'));

