update eg_role set name='CITIZEN' where name='Citizen';
update eg_role set name='EMPLOYEE' where name='Employee';
update eg_role set name='SYSTEM' where name='Super User';
update eg_role set name='BUSINESS' where name='Business User';

delete from eg_userrole  where roleid = (select id from eg_role where name='SYSTEM');
insert into eg_userrole(userid, roleid) values ((select id from eg_user where username='egovernments'), (select id from eg_role where name='SYSTEM'));