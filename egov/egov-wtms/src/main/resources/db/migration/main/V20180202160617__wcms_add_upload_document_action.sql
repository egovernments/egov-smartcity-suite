
INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version,createdby, createddate, lastmodifiedby, lastmodifieddate,application) VALUES (nextval('SEQ_EG_ACTION'), 'UploadWaterConnectionDocument', '/application/watercharges/uploaddocument', null,(select id from eg_module where name='WaterTaxTransactions'), 1, 'Upload Water Connection Document', false,'wtms', 0, 1, now(), 1, now(),(select id from eg_module where name='Water Tax Management'));

INSERT INTO eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Water Tax Approver'),
(select id from eg_action where name='UploadWaterConnectionDocument'));

INSERT INTO eg_roleaction (roleid,actionid) values ((select id from eg_role where name='ULB Operator'),
(select id from eg_action where name='UploadWaterConnectionDocument'));
