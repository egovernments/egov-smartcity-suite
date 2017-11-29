Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values 
(NEXTVAL('SEQ_EG_ACTION'),'EditDemand-wcms','/search/waterSearch/commonSearch/', 'applicationType=EDITDEMAND',(select id from EG_MODULE where name = 'WaterTaxTransactions'),null,
'Add/Edit Demand',true,'wtms',0,1,now(),1,now(),(select id from eg_module  where name = 'Water Tax Management'));


INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name = 'SYSTEM'),(select id FROM eg_action WHERE NAME = 'EditDemand-wcms' and CONTEXTROOT='wtms'));


Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Property Administrator'),(select id from eg_action where name='EditDemand-wcms' and CONTEXTROOT='wtms'));