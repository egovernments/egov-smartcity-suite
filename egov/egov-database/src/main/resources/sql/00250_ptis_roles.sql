 
INSERT INTO EG_ROLE (ID, NAME, DESCRIPTION, CREATEDDATE, CREATEDBY, LASTMODIFIEDDATE, LASTMODIFIEDBY) VALUES (NEXTVAL('SEQ_EG_ROLE'), 'Data Entry Operator', 'Data entry operator', current_timestamp, 1, current_timestamp, 1);

INSERT INTO EG_ROLE (ID, NAME, DESCRIPTION, CREATEDDATE, CREATEDBY, LASTMODIFIEDDATE, LASTMODIFIEDBY) VALUES (NEXTVAL('SEQ_EG_ROLE'), 'PTIS Verifier', 'Muncipal Commissioner, final approval authority', current_timestamp, 1, current_timestamp, 1);

INSERT INTO EG_ROLE (ID, NAME, DESCRIPTION, CREATEDDATE, CREATEDBY, LASTMODIFIEDDATE, LASTMODIFIEDBY) VALUES (NEXTVAL('SEQ_EG_ROLE'), 'PTIS Approver', 'Muncipal Commissioner, final approval authority', current_timestamp, 1, current_timestamp, 1);

update eg_roleaction set roleid = (select id from eg_role where name='Data Entry Operator') where roleid = (select id from eg_role where name='assessor');

delete from eg_userrole where userid = (select id from eg_user where username='assessor');
delete from eg_role where name ='assessor';
delete from eg_user where username='assessor';

insert into eg_userrole (roleid,userid) values ((select id from eg_role where name='Data Entry Operator'),(select id from eg_user where username='manasa'));
insert into eg_userrole (roleid,userid) values ((select id from eg_role where name='Data Entry Operator'),(select id from eg_user where username='subhash'));
insert into eg_userrole (roleid,userid) values ((select id from eg_role where name='Data Entry Operator'),(select id from eg_user where username='nayeemalla'));
insert into eg_userrole (roleid,userid) values ((select id from eg_role where name='Data Entry Operator'),(select id from eg_user where username='malathi'));
insert into eg_userrole (roleid,userid) values ((select id from eg_role where name='Data Entry Operator'),(select id from eg_user where username='parvati'));

insert into eg_userrole (roleid,userid) values ((select id from eg_role where name='PTIS Verifier'),(select id from eg_user where username='manikanta'));
insert into eg_userrole (roleid,userid) values ((select id from eg_role where name='PTIS Verifier'),(select id from eg_user where username='ramakrishna'));

insert into eg_userrole (roleid,userid) values ((select id from eg_role where name='PTIS Verifier'),(select id from eg_user where username='satyam'));
