Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'closurewatertaxestimationnoticeview','/application/acknowlgementNotice/view/',null,(select id from eg_module where name='WaterTaxTransactions'),null,'closurewatertaxestimationnoticeview','false','wtms',0,1,to_timestamp('2015-08-19 10:45:50.991631','null'),1,to_timestamp('2015-08-19 10:45:50.991631','null'),(select id from eg_module where name='Water Tax Management'));



Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'Reconnwatertaxestimationnoticeview','/application/ReconnacknowlgementNotice/view/',null,(select id from eg_module where name='WaterTaxTransactions'),null,'Reconnwatertaxestimationnoticeview','false','wtms',0,1,to_timestamp('2015-08-19 10:45:50.991631','null'),1,to_timestamp('2015-08-19 10:45:50.991631','null'),(select id from eg_module where name='Water Tax Management'));


Insert into eg_roleaction (roleid,actionid) values 
((select id from eg_role where name='Super User'),
(select id from eg_action where name='closurewatertaxestimationnoticeview'));
Insert into eg_roleaction (roleid,actionid) values 
((select id from eg_role where name='ULB Operator'),
(select id from eg_action where name='closurewatertaxestimationnoticeview'));
Insert into eg_roleaction (roleid,actionid) values 
((select id from eg_role where name='Water Tax Approver'),
(select id from eg_action where name='closurewatertaxestimationnoticeview'));
Insert into eg_roleaction (roleid,actionid) values 
((select id from eg_role where name='CSC Operator'),
(select id from eg_action where name='closurewatertaxestimationnoticeview'));

Insert into eg_roleaction (roleid,actionid) values 
((select id from eg_role where name='Super User'),
(select id from eg_action where name='Reconnwatertaxestimationnoticeview'));
Insert into eg_roleaction (roleid,actionid) values 
((select id from eg_role where name='ULB Operator'),
(select id from eg_action where name='Reconnwatertaxestimationnoticeview'));
Insert into eg_roleaction (roleid,actionid) values 
((select id from eg_role where name='Water Tax Approver'),
(select id from eg_action where name='Reconnwatertaxestimationnoticeview'));
Insert into eg_roleaction (roleid,actionid) values 
((select id from eg_role where name='CSC Operator'),
(select id from eg_action where name='Reconnwatertaxestimationnoticeview'));