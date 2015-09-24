INSERT INTO eg_roleaction (actionid, roleid) values ((select id from eg_action where name = 'Create New License'), (select id from eg_role where name = 'TLCreator'));
INSERT INTO eg_roleaction (actionid, roleid) values ((select id from eg_action where name = 'AjaxDesignationDropdown'), (select id from eg_role where name = 'TLCreator'));
INSERT INTO eg_roleaction (actionid, roleid) values ((select id from eg_action where name = 'AjaxApproverDropdown'), (select id from eg_role where name = 'TLCreator'));
INSERT INTO eg_roleaction (actionid, roleid) values ((select id from eg_action where name = 'SearchTradeLicense'), (select id from eg_role where name = 'TLCreator'));
INSERT INTO eg_roleaction (actionid, roleid) values ((select id from eg_action where name = 'SearchTradeLicense'), (select id from eg_role where name = 'TLApprover'));
INSERT INTO eg_roleaction (actionid, roleid) values ((select id from eg_action where name = 'searchTrade-search'), (select id from eg_role where name = 'TLCreator'));
INSERT INTO eg_roleaction (actionid, roleid) values ((select id from eg_action where name = 'searchTrade-search'), (select id from eg_role where name = 'TLApprover'));
INSERT INTO eg_roleaction (actionid, roleid) values ((select id from eg_action where name = 'viewTradeLicense-view'), (select id from eg_role where name = 'TLCreator'));
INSERT INTO eg_roleaction (actionid, roleid) values ((select id from eg_action where name = 'viewTradeLicense-view'), (select id from eg_role where name = 'TLApprover'));
INSERT INTO eg_roleaction (actionid, roleid) values ((select id from eg_action where name = 'viewTradeLicense-view'), (select id from eg_role where name = 'Super User'));

DELETE FROM eg_roleaction  where actionid = (select id from eg_action where name = 'View Trade License');
DELETE FROM eg_roleaction  where actionid = (select id from eg_action where name = 'View Trade License View Action');
DELETE FROM eg_action where name = 'View Trade License';
DELETE FROM eg_action where name = 'View Trade License View Action';