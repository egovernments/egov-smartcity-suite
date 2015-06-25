delete from eg_roleaction  where actionid = (select id from eg_action where "name"='loginForm');
delete from eg_action where "name"='loginForm';
delete from eg_action where "name"='RecoverPassword';
delete from eg_action where "name"='ResetPassword';
update eg_action set "name"='Official Home Page' where "name" ='LoginForm';
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where "name"='Employee'), (select id from eg_action where "name"='Official Home Page'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where "name"='Employee'), (select id from eg_action where "name"='File Download'));
