--Roleaction mapping of Search Trade License for ULB and CSC operation for collection

INSERT INTO eg_roleaction (actionid, roleid) values ((select id from eg_action where name = 'SearchTradeLicense'), 
(select id from eg_role where name = 'ULB Operator'));
INSERT INTO eg_roleaction (actionid, roleid) values ((select id from eg_action where name = 'SearchTradeLicense'), 
(select id from eg_role where name = 'CSC Operator'));


INSERT INTO eg_roleaction (actionid, roleid) values ((select id from eg_action where name = 'searchTrade-search'), 
(select id from eg_role where name = 'ULB Operator'));
INSERT INTO eg_roleaction (actionid, roleid) values ((select id from eg_action where name = 'searchTrade-search'), 
(select id from eg_role where name = 'CSC Operator'));


INSERT INTO eg_roleaction (actionid, roleid) values ((select id from eg_action where name = 'Trade License Bill Collect'), 
(select id from eg_role where name = 'ULB Operator'));
INSERT INTO eg_roleaction (actionid, roleid) values ((select id from eg_action where name = 'Trade License Bill Collect'), 
(select id from eg_role where name = 'CSC Operator'));
