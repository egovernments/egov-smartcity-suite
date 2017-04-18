INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Sewerage Tax Administrator'), (select id from eg_action where name = 'AjaxCheckConnection'));
