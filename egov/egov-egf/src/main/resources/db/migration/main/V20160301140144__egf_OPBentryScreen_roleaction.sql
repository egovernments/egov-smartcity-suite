


INSERT INTO eg_roleaction (roleid, actionid) 
VALUES ( (select id from eg_role where name='Financial Administrator'),(select id from eg_action where name='ajax-common-entityby20'));
INSERT INTO eg_roleaction (roleid, actionid) 
VALUES ( (select id from eg_role where name='Financial Administrator'),(select id from eg_action where name='searchEntries-accountdetail'));
