INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version,createdby, createddate, lastmodifiedby, lastmodifieddate,application) VALUES (nextval('SEQ_EG_ACTION'), 'EstimationNoticeGenerate', '/application/generateestimationnotice', null,(select id from eg_module where name='WaterTaxTransactions'), 3, 'Generate Estimation Notice', true,'wtms', 0, 1, now(), 1, now(),(select id from eg_module where name='Water Tax Management'));

INSERT INTO eg_roleaction (roleid,actionid) values ((select id from eg_role where name='SYSTEM'),
(select id from eg_action where name='EstimationNoticeGenerate'));

INSERT INTO eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Water Tax Report Viewer'),
(select id from eg_action where name='EstimationNoticeGenerate'));




INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version,createdby, createddate, lastmodifiedby, lastmodifieddate,application) VALUES (nextval('SEQ_EG_ACTION'), 'EstimationNoticeResult', '/application/result', null,(select id from eg_module where name='WaterTaxTransactions'), 3, 'EstimationNoticeResult', false,'wtms', 0, 1, now(), 1, now(),(select id from eg_module where name='Water Tax Management'));

INSERT INTO eg_roleaction (roleid,actionid) values ((select id from eg_role where name='SYSTEM'),
(select id from eg_action where name='EstimationNoticeResult'));

INSERT INTO eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Water Tax Report Viewer'),
(select id from eg_action where name='EstimationNoticeResult'));





INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version,createdby, createddate, lastmodifiedby, lastmodifieddate,application) VALUES (nextval('SEQ_EG_ACTION'), 'EstimationNoticeDownload', '/application/waterTax/downloadEstimationNotice', null,(select id from eg_module where name='WaterTaxTransactions'), 3, 'EstimationNoticeDownload', false,'wtms', 0, 1, now(), 1, now(),(select id from eg_module where name='Water Tax Management'));

INSERT INTO eg_roleaction (roleid,actionid) values ((select id from eg_role where name='SYSTEM'),
(select id from eg_action where name='EstimationNoticeDownload'));

INSERT INTO eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Water Tax Report Viewer'),
(select id from eg_action where name='EstimationNoticeDownload'));
