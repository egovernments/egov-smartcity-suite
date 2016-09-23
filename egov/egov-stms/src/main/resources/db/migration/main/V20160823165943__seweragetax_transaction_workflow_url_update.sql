INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Sewerage Tax Creator'), (select id from eg_action where name = 'AjaxDesignationsByDepartment' and contextroot = 'eis')); 

INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Sewerage Tax Creator'), (select id from eg_action where name = 'AjaxApproverByDesignationAndDepartment' and contextroot = 'eis')); 

--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Sewerage Tax Creator') and actionid = (select id from eg_action where name ='AjaxDesignationsByDepartment' and contextroot = 'eis');
--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Sewerage Tax Creator') and actionid = (select id from eg_action where name ='AjaxApproverByDesignationAndDepartment' and contextroot = 'eis');
