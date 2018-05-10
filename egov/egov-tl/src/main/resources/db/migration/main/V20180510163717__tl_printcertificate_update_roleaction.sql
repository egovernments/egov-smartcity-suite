--Missing roleaction for print certificate

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((select id from eg_role where name like 'CSC Operator'),(select id from eg_action where name='View Trade License Generate Certificate'));
