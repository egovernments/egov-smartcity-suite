INSERT INTO  eg_roleaction (actionid, roleid) values ((select id from eg_action where name = 'EmpSearchAjax'),(select id from eg_role where name in ('Employee Admin')));

INSERT INTO  eg_roleaction (actionid, roleid) values ((select id from eg_action where name = 'Update Employee'),(select id from eg_role where name in ('Employee Admin')));

INSERT INTO  eg_roleaction (actionid, roleid) values ((select id from eg_action where name = 'View Employee'),(select id from eg_role where name in ('Employee Admin')));