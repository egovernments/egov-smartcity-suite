insert into eg_userrole (roleid, userid) values((select id from eg_role where name='Citizen'),(select id from eg_user where username='9999999999'));
insert into eg_userrole (roleid, userid) values((select id from eg_role where name='Citizen'),(select id from eg_user where username='8888888888'));
--delete from eg_userrole where userid in (select id from eg_user where username in ('8888888888','9999999999'));
