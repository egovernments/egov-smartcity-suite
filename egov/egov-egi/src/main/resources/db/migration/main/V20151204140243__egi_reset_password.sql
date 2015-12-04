INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, 
createddate, lastmodifiedby, lastmodifieddate, application) 
VALUES (nextval('seq_eg_action'), 'ResetPassword', '/user/reset-password', NULL, 
(select id from eg_module where name='User Module'), 0, 'Reset Password', true, 'egi', 0, 1, 
'2015-08-28 10:43:35.552035', 1, '2015-08-28 10:43:35.552035', 1);

INSERT INTO EG_ROLEACTION (roleid,actionid) values ((select id from eg_role where name='Super User'), 
(select id from eg_action where name='ResetPassword'));
