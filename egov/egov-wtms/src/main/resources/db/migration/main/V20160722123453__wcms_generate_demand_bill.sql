
update eg_action set displayname = 'Search Bill' where name = 'GenerateBillReport' and contextroot ='wtms';

Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values 
(NEXTVAL('SEQ_EG_ACTION'),'GenerateBillForConsumerCode','/search/waterSearch/commonSearch/', 'applicationType=GENERATEBILL',(select id from eg_module
 where name='WaterTaxReports'),null,
'Generate Bill','t','wtms',0,1,now(),1,now(),(select id from eg_module  where name = 'Water Tax Management'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) = 'SUPER USER'),(select id FROM eg_action WHERE NAME = 'GenerateBillForConsumerCode' and CONTEXTROOT='wtms'));
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Property Administrator'),(select id from eg_action where name='GenerateBillForConsumerCode' and CONTEXTROOT='wtms'));

Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),
'GenerateBillForHSCNO','/report/generateBillForHSCNo/',null,(select id from eg_module where name='WaterTaxReports'),null,'Generate Bill For ConsumerCode','false','wtms',0,1,to_timestamp('2015-08-15 11:02:43.968604',
 'null'),1,to_timestamp('2015-08-15 11:02:43.968604','null'),(select id from eg_module where name='Water Tax Management'));

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Super User'),(select id from eg_action where name='GenerateBillForHSCNO'));

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Property Administrator'),(select id from eg_action where name='GenerateBillForHSCNO' and CONTEXTROOT='wtms'));