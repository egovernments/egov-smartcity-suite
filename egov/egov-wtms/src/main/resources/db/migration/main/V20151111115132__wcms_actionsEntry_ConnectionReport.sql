
INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version,
 createdby, createddate, lastmodifiedby, lastmodifieddate,application)
    VALUES (nextval('seq_eg_action'), 'WaterTaxConnectionReportWardWise', '/reports/coonectionReport/wardWise', null,
     (select id from eg_module where name='WaterTaxReports'), null, 'Number of connections', true,
     'wtms', 0, 1, now(), 1, now(),(Select id from eg_module where name='Water Tax Management' and parentmodule is null));


INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where UPPER(name) = 'SUPER USER'),(select id FROM eg_action 
 WHERE NAME = 'WaterTaxConnectionReportWardWise' and CONTEXTROOT='wtms'));