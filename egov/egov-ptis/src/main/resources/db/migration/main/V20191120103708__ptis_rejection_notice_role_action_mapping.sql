

INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate,application) 
VALUES (nextval('seq_eg_action'), 'Generate Rejection Notice', '/rejectionnotice/generaterejectionnotice', null,(select id from eg_module where name='Existing property'), 1, 'Generate Rejection Notice', false, 'ptis', 0, 1, now(), 1, now(),(select id from eg_module where name='Property Tax' and parentmodule is null));
INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Generate Rejection Notice'), id from eg_role where name in ('Property Administrator','Property Approver','Property Verifier');
