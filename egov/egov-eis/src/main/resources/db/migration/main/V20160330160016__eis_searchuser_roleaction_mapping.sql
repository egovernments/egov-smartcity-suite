INSERT INTO  eg_roleaction (actionid, roleid) values ((select id from eg_action where name = 'ajaxuserlist'),(select id from eg_role where name in ('Employee Admin')));

INSERT INTO  eg_roleaction (actionid, roleid) values ((select id from eg_action where name = 'AjaxLoadRoleByUser'),(select id from eg_role where name in ('Employee Admin')));

INSERT INTO  eg_roleaction (actionid, roleid) values ((select id from eg_action where name = 'UpdateuserRole'), (select id from eg_role where name in ('Employee Admin')));

INSERT INTO  eg_roleaction (actionid, roleid) values ((select id from eg_action where name = 'UpdateuserRoleForm'),(select id from eg_role where name in ('Employee Admin')));

INSERT INTO  eg_roleaction (actionid, roleid) values ((select id from eg_action where name = 'viewuserRole'),(select id from eg_role where name in ('Employee Admin')));

