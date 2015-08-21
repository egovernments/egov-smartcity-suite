update eg_action set name = 'WaterTaxDCBReportZoneWise',url='/reports/dCBReport/zoneWise',ordernumber = 1 where name = 'WaterTaxDCBReport';



Insert into eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) 
values (nextval('SEQ_EG_ACTION'),'WaterTaxDCBReportWardWise','/reports/dCBReport/wardWise', null, (select id from eg_module where name = 'WaterTaxReports'), 2, 'DCB Report Ward Wise',
 true, 'wtms', 0, 1, current_timestamp,1,current_timestamp, (select id from eg_module where name = 'Water Tax Management'));



INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'WaterTaxDCBReportWardWise'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Water Tax Report Viewer') ,(select id FROM eg_action  WHERE name = 'WaterTaxDCBReportWardWise'));


Insert into eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) 
values (nextval('SEQ_EG_ACTION'),'WaterTaxDCBReportBlockWise','/reports/dCBReport/blockWise', null, (select id from eg_module where name = 'WaterTaxReports'), 3, 'DCB Report Block Wise',
 true, 'wtms', 0, 1, current_timestamp,1,current_timestamp, (select id from eg_module where name = 'Water Tax Management'));



INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'WaterTaxDCBReportBlockWise'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Water Tax Report Viewer') ,(select id FROM eg_action  WHERE name = 'WaterTaxDCBReportBlockWise'));
