update eg_user set type = 'CITIZEN' ,password = 'DTRJto+XfKQ=' where username like 'citizenUser';

insert into eg_userrole values((select id from eg_role where UPPER(name) LIKE 'SUPERUSER') ,(select id from eg_user where username LIKE 'citizenUser') );
