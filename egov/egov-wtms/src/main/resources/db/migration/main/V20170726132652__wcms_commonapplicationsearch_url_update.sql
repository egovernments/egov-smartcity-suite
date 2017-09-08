update eg_action set url = '/search/waterSearch/commonSearch/meterentry' , queryparams=null where name='EnterMeterEntryForConnection' and contextroot='wtms';
update eg_action set url = '/search/waterSearch/commonSearch/closureconnection' , queryparams=null where name='createClosureConnection' and contextroot='wtms';
update eg_action set url = '/search/waterSearch/commonSearch/changeofuse' , queryparams=null where name='CreateChangeOfUseConnection' and contextroot='wtms';
update eg_action set url = '/search/waterSearch/commonSearch/additionalconnection' , queryparams=null where name='CreateAdditionalConnection' and contextroot='wtms';
update eg_action set url = '/search/waterSearch/commonSearch/collecttax' , queryparams=null where name='collectTaxForwatrtax' and contextroot='wtms';
update eg_action set url = '/search/waterSearch/commonSearch/reconnection' , queryparams=null where name='createReConnection' and contextroot='wtms';
update eg_action set url = '/search/waterSearch/commonSearch/editcollection' , queryparams=null where name='EditCollection' and contextroot='wtms';
update eg_action set url = '/search/waterSearch/commonSearch/dataentryedit' , queryparams=null where name='modifydataentryconnectiondetails' and contextroot='wtms';
update eg_action set url = '/search/waterSearch/commonSearch/generatebill' , queryparams=null where name='GenerateBillForConsumerCode' and contextroot='wtms';

Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'CommonWaterTaxSearchScreen','/search/waterSearch/commonSearch-form/', null,(select id from EG_MODULE where name = 'WaterTaxTransactions'),null,'Common Water Tax Search Screen','t','wtms',0,1,now(),1,now(),(select id from eg_module  where name = 'Water Tax Management'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name = 'ULB Operator'),(select id FROM eg_action  WHERE NAME = 'CommonWaterTaxSearchScreen' and CONTEXTROOT='wtms'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) = 'CSC OPERATOR'),(select id FROM eg_action WHERE NAME = 'CommonWaterTaxSearchScreen' and CONTEXTROOT='wtms'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name = 'SYSTEM'),(select id FROM eg_action  WHERE NAME = 'CommonWaterTaxSearchScreen' and CONTEXTROOT='wtms'));


--rollback delete from eg_roleaction where roleid in ((select id from eg_role where name = 'ULB Operator'),(select id from eg_role where UPPER(name) = 'CSC OPERATOR'),(select id from eg_role where name = 'SYSTEM')) and actionid = (select id FROM eg_action  WHERE NAME = 'CommonWaterTaxSearchScreen' and CONTEXTROOT='wtms');