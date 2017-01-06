INSERT INTO eg_roleaction (roleid, actionid)
SELECT (select id from eg_role where name='DPO Modification Features'), (select id from eg_action where name='Update Employee')
WHERE
    NOT EXISTS (select roleid,actionid from eg_roleaction where roleid in (select id from eg_role where name='DPO Modification Features') and actionid in 
 (select id from eg_action where name='Update Employee'));

INSERT INTO eg_roleaction (roleid, actionid)
SELECT (select id from eg_role where name='DPO Modification Features'), (select id from eg_action where name='EmpSearchAjax')
WHERE
    NOT EXISTS (select roleid,actionid from eg_roleaction where roleid in (select id from eg_role where name='DPO Modification Features') and actionid in 
 (select id from eg_action where name='EmpSearchAjax'));