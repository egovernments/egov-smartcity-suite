
Insert into eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) 
values (nextval('SEQ_EG_ACTION'),'ajaxForExistredMeterReading','/ajax-meterReadingEntryExist', null, 
(select id from eg_module where name = 'WaterTaxTransactions'), 4, 'ajaxForExistredMeterReading',
 false, 'wtms', 0, 1, current_timestamp,1,current_timestamp, (select id from eg_module where name = 'Water Tax Management'));



INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'ajaxForExistredMeterReading'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'ULB OPERATOR') ,(select id FROM eg_action  WHERE name = 'ajaxForExistredMeterReading'));
