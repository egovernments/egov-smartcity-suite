INSERT INTO eg_roleaction (roleid, actionid)
select (select id from eg_role where name = 'Marriage Registration Approver'),(select id from eg_action where name ='calculateMarriageFee' and contextroot = 'mrs')
WHERE
    NOT EXISTS (select roleid,actionid from eg_roleaction where roleid in (select id from eg_role where name='Marriage Registration Approver') and actionid in 
 (select id from eg_action where name='calculateMarriageFee'));

INSERT INTO eg_roleaction (roleid, actionid)
select (select id from eg_role where name = 'Marriage Registration Approver'),(select id from eg_action where name ='AjaxDesignationsByDepartment')
WHERE
    NOT EXISTS (select roleid,actionid from eg_roleaction where roleid in (select id from eg_role where name='Marriage Registration Approver') and actionid in 
 (select id from eg_action where name='AjaxDesignationsByDepartment'));

INSERT INTO eg_roleaction (roleid, actionid)
select (select id from eg_role where name = 'Marriage Registration Approver'),(select id from eg_action where name ='AjaxApproverByDesignationAndDepartment')
WHERE
    NOT EXISTS (select roleid,actionid from eg_roleaction where roleid in (select id from eg_role where name='Marriage Registration Approver') and actionid in 
 (select id from eg_action where name='AjaxApproverByDesignationAndDepartment'));