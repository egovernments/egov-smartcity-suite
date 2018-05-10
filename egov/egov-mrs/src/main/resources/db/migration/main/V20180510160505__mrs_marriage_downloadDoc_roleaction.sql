INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, "version", createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'),'Download Marriage documents', '/registration/downloadMarriagefile', NULL,(SELECT id FROM eg_module WHERE name = 'MR-Transactions'), 11, 'Download Marriage documents', false, 'mrs', 0, 1, now(), 1, now(), (select id from eg_module where name='Marriage Registration'));

INSERT INTO eg_roleaction(roleid,actionid)  VALUES((select id from eg_role  where name ='Marriage Registration Approver'),(select id from eg_action where name ='Download Marriage documents'));
INSERT INTO eg_roleaction(roleid,actionid)  VALUES((select id from eg_role  where name ='Marriage Registrar'),(select id from eg_action where name ='Download Marriage documents'));

ALTER TABLE egmrs_registration ADD COLUMN datasheetfilestore bigint;
