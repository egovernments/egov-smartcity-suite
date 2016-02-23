

INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='Load all bank across fund'));

