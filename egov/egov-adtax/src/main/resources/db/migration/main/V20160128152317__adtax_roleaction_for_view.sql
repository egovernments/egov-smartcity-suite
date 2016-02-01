delete from eg_roleaction where actionid =(select id from eg_action where name='adtaxview');
delete from eg_action where name='adtaxview';
delete from eg_roleaction where actionid =(select id from eg_action where name='HoardingLegacyview');
delete from eg_action where name='HoardingLegacyview';

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Advertisement Tax Creator') ,(select id FROM eg_action  WHERE name = 'HoardingView'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Advertisement Tax Admin') ,(select id FROM eg_action  WHERE name = 'HoardingView'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Collection Operator') ,(select id FROM eg_action  WHERE name = 'collectTaxByAgency'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Advertisement Tax Approver') ,(select id FROM eg_action  WHERE name = 'calculateTaxAmount'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Collection Operator') ,(select id FROM eg_action  WHERE name = 'AgencyAjaxDropdown'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Advertisement Tax Admin') ,(select id FROM eg_action  WHERE name = 'permitorderreport'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Advertisement Tax Admin') ,(select id FROM eg_action  WHERE name = 'demandnoticereport'));
