Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) 
values (nextval('SEQ_EG_ACTION'),'Collect Water Estimation Charges','/estimationcharges/search',null,(select id from eg_module where name='WaterTaxTransactions'),null,'Collect Water Estimation Charges','true','wtms',0,(select id from eg_user where username ='egovernments'),now(),(select id from eg_user where username ='egovernments'),now(),(select id from eg_module where name='Water Tax Management'));

INSERT INTO EG_ROLEACTION (ACTIONID ,ROLEID) 
select (select id FROM eg_action WHERE name = 'Collect Water Estimation Charges'), roleid FROM eg_roleaction where actionid in (select id from eg_action where name='collectTaxForwatrtax');

Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) 
values (nextval('SEQ_EG_ACTION'),'Water Estimation Charges Verification','/estimationcharges/verification',null,(select id from eg_module where name='WaterTaxTransactions'),null,'Water Estimation Charges Verification','false','wtms',0,(select id from eg_user where username ='egovernments'),now(),(select id from eg_user where username ='egovernments'),now(),(select id from eg_module where name='Water Tax Management'));

INSERT INTO EG_ROLEACTION (ACTIONID ,ROLEID) 
select (select id FROM eg_action WHERE name = 'Water Estimation Charges Verification'), roleid FROM eg_roleaction where actionid in (select id from eg_action where name='collectTaxForwatrtax');


Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) 
values (nextval('SEQ_EG_ACTION'),'Estimation Charges Collection','/estimationcharges/collection',null,(select id from eg_module where name='WaterTaxTransactions'),null,'Estimation Charges Collection','false','wtms',0,(select id from eg_user where username ='egovernments'),now(),(select id from eg_user where username ='egovernments'),now(),(select id from eg_module where name='Water Tax Management'));

INSERT INTO EG_ROLEACTION (ACTIONID ,ROLEID) 
select (select id FROM eg_action WHERE name = 'Estimation Charges Collection'), roleid FROM eg_roleaction where actionid in (select id from eg_action where name='collectTaxForwatrtax');

update eg_action set displayname ='Collect Water Charges' where name='collectTaxForwatrtax' and contextroot='wtms';

