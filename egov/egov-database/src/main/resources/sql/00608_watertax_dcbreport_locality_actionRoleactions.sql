Insert into eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) 
values (nextval('SEQ_EG_ACTION'),'WaterTaxDCBReportLocalityWise','/reports/dCBReport/localityWise', null, (select id from eg_module where name = 'WaterTaxReports'), 4, 'DCB Report Locality Wise',
 true, 'wtms', 0, 1, current_timestamp,1,current_timestamp, (select id from eg_module where name = 'Water Tax Management'));



INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'WaterTaxDCBReportLocalityWise'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Water Tax Report Viewer') ,(select id FROM eg_action  WHERE name = 'WaterTaxDCBReportLocalityWise'));

