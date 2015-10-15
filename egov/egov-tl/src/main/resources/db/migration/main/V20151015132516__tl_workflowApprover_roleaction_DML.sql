-- Workflow designation and approver dropdown roleaction
INSERT INTO eg_roleaction (actionid, roleid) values ((select id from eg_action where name = 'AjaxDesignationDropdown'), (select id from eg_role where name = 'TLCreator'));
INSERT INTO eg_roleaction (actionid, roleid) values ((select id from eg_action where name = 'AjaxApproverDropdown'), (select id from eg_role where name = 'TLCreator'));

INSERT INTO eg_roleaction (actionid, roleid) values ((select id from eg_action where name = 'AjaxDesignationDropdown'), (select id from eg_role where name = 'TLApprover'));
INSERT INTO eg_roleaction (actionid, roleid) values ((select id from eg_action where name = 'AjaxApproverDropdown'), (select id from eg_role where name = 'TLApprover'));
