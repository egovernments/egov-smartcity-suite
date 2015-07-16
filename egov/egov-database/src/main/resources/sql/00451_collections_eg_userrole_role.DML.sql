
INSERT INTO EG_ROLE (ID, NAME, DESCRIPTION, CREATEDDATE, CREATEDBY, LASTMODIFIEDDATE, LASTMODIFIEDBY) VALUES (NEXTVAL('SEQ_EG_ROLE'), 'Remitter', 'Remitter', current_timestamp, 1, current_timestamp, 1);

insert into eg_userrole (roleid,userid) values ((select id from eg_role where name='Remitter'),(select id from eg_user where username='narasappa'));
