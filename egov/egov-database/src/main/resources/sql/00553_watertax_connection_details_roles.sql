INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Water Tax Approver') ,(select id FROM eg_action  WHERE name = 'pipesizesbypropertytypeajax'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'ULB Operator') ,(select id FROM eg_action  WHERE name = 'pipesizesbypropertytypeajax'));


INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Water Tax Approver') ,(select id FROM eg_action  WHERE name = 'usagetypebypropertytypeajax'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'ULB Operator') ,(select id FROM eg_action  WHERE name = 'usagetypebypropertytypeajax'));


INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Water Tax Approver') ,(select id FROM eg_action  WHERE name = 'categorytypebypropertytypeajax'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'ULB Operator') ,(select id FROM eg_action  WHERE name = 'categorytypebypropertytypeajax'));