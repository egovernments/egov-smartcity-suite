update eg_user set username='9999999999' where username='anonymous';

insert into eg_userrole values((select id from eg_role where name='Citizen'),(select id from eg_user where username='9999999999'));
