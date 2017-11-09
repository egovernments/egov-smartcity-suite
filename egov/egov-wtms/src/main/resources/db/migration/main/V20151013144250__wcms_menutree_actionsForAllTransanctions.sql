Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values 
(NEXTVAL('SEQ_EG_ACTION'),'CreateAdditionalConnection','/search/waterSearch/commonSearch/', 'applicationType=ADDNLCONNECTION',(select id from EG_MODULE where name = 'WaterTaxTransactions'),null,
'Create Additional Connection','t','wtms',0,1,now(),1,now(),(select id from eg_module  where name = 'Water Tax Management'));


Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values 
(NEXTVAL('SEQ_EG_ACTION'),'CreateChangeOfUseConnection','/search/waterSearch/commonSearch/', 'applicationType=CHANGEOFUSE',(select id from EG_MODULE where name = 'WaterTaxTransactions'),null,
'Create ChangeOfUse Connection','t','wtms',0,1,now(),1,now(),(select id from eg_module  where name = 'Water Tax Management'));


Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values 
(NEXTVAL('SEQ_EG_ACTION'),'createClosureConnection','/search/waterSearch/commonSearch/', 'applicationType=CLOSURECONNECTION',(select id from EG_MODULE where name = 'WaterTaxTransactions'),null,
'Create Closure Connection','t','wtms',0,1,now(),1,now(),(select id from eg_module  where name = 'Water Tax Management'));


Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values 
(NEXTVAL('SEQ_EG_ACTION'),'EnterMeterEntryForConnection','/search/waterSearch/commonSearch/', 'applicationType=METERENTRY',(select id from EG_MODULE where name = 'WaterTaxTransactions'),null,
'Enter Meter Entry For Connection','t','wtms',0,1,now(),1,now(),(select id from eg_module  where name = 'Water Tax Management'));



Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values 
(NEXTVAL('SEQ_EG_ACTION'),'collectTaxForwatrtax','/search/waterSearch/commonSearch/', 'applicationType=COLLECTTAX',
(select id from EG_MODULE where name = 'WaterTaxTransactions'),null,
'Collect Charges','t','wtms',0,1,now(),1,now(),(select id from eg_module  where name = 'Water Tax Management'));



INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where name = 'ULB Operator'),(select id FROM eg_action  
WHERE NAME = 'CreateAdditionalConnection' and CONTEXTROOT='wtms'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where UPPER(name) = 'CSC OPERATOR'),(select id FROM eg_action 
 WHERE NAME = 'CreateAdditionalConnection' and CONTEXTROOT='wtms'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where name = 'ULB Operator'),(select id FROM eg_action  
WHERE NAME = 'CreateChangeOfUseConnection' and CONTEXTROOT='wtms'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where UPPER(name) = 'CSC OPERATOR'),(select id FROM eg_action 
 WHERE NAME = 'CreateChangeOfUseConnection' and CONTEXTROOT='wtms'));


INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where name = 'ULB Operator'),(select id FROM eg_action  
WHERE NAME = 'createClosureConnection' and CONTEXTROOT='wtms'));



INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where name = 'ULB Operator'),(select id FROM eg_action  
WHERE NAME = 'EnterMeterEntryForConnection' and CONTEXTROOT='wtms'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where UPPER(name) = 'CSC OPERATOR'),(select id FROM eg_action 
 WHERE NAME = 'collectTaxForwatrtax' and CONTEXTROOT='wtms'));
 
update eg_appconfig_values set value='CSC Operator' where key_id in(select id from eg_appconfig where key_name='ROLEFORNONEMPLOYEEINWATERTAX');
 
delete from eg_roleaction where actionid in(select id from eg_action where url='/search/waterSearch/commonSearch/' 
and name='createReConnection') and roleid in(select id from eg_role where name='CSC Operator');