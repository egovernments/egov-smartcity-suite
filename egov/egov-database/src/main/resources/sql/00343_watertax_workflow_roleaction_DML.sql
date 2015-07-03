INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate)
    VALUES (nextval('seq_eg_action'), 'AjaxDesignationsByDepartment', '/ajax-designationsByDepartment', null, (select id from eg_module where name='WaterTaxApplication'), 1, 'Approver Designations by Department', false, 'wtms', 0, 1, now(), 1, now());

INSERT INTO eg_roleaction (actionid,roleid) VALUES ((SELECT id FROM eg_action WHERE name='AjaxDesignationsByDepartment' and contextroot='wtms'),
(SELECT id FROM eg_role where name='Super User'));
INSERT INTO eg_roleaction (actionid,roleid) VALUES ((SELECT id FROM eg_action WHERE name='AjaxDesignationsByDepartment' and contextroot='wtms'),
(SELECT id FROM eg_role where name='CSC Operator'));
INSERT INTO eg_roleaction (actionid,roleid) VALUES ((SELECT id FROM eg_action WHERE name='AjaxDesignationsByDepartment' and contextroot='wtms'),
(SELECT id FROM eg_role where name='Water Tax Approver'));

INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate)
    VALUES (nextval('seq_eg_action'), 'AjaxApproverByDesignationAndDepartment', '/ajax-positionsByDepartmentAndDesignation', null, (select id from eg_module where name='WaterTaxApplication'), 1, 'Approver By Designation and Department', false, 'wtms', 0, 1, now(), 1, now());

INSERT INTO eg_roleaction (actionid,roleid) VALUES ((SELECT id FROM eg_action WHERE name='AjaxApproverByDesignationAndDepartment' and contextroot='wtms'),
(SELECT id FROM eg_role where name='Super User'));
INSERT INTO eg_roleaction (actionid,roleid) VALUES ((SELECT id FROM eg_action WHERE name='AjaxApproverByDesignationAndDepartment' and contextroot='wtms'),
(SELECT id FROM eg_role where name='CSC Operator'));
INSERT INTO eg_roleaction (actionid,roleid) VALUES ((SELECT id FROM eg_action WHERE name='AjaxApproverByDesignationAndDepartment' and contextroot='wtms'),
(SELECT id FROM eg_role where name='Water Tax Approver'));

--rollback delete from EG_ROLEACTION where ROLEID in(select id from eg_role where name in ('Super User','CSC Operator','Water Tax Approver')) and actionid in (select id FROM eg_action  WHERE name='AjaxApproverByDesignationAndDepartment' and contextroot='wtms');
--rollback delete from eg_action where name='AjaxApproverByDesignationAndDepartment' and contextroot='wtms';
--rollback delete from EG_ROLEACTION where ROLEID in(select id from eg_role where name in ('Super User','CSC Operator','Water Tax Approver')) and actionid in (select id FROM eg_action  WHERE name='AjaxDesignationsByDepartment' and contextroot='wtms');
--rollback delete from eg_action where name='AjaxDesignationsByDepartment' and contextroot='wtms';