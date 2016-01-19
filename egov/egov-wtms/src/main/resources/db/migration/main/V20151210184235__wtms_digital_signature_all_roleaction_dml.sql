------Water Tax Digital Signature Pending Form------------
INSERT INTO EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) 
values (nextval('SEQ_EG_ACTION'),'Water Tax Search Pending Digital Signature','/digitalSignature/digitalSignaturePending-form',
null,(select id from eg_module where name='WaterTaxTransactions'),null,'Pending Digital Signature','true','wtms',0,1,to_timestamp('2015-08-15 11:04:14.873066','null'),1,to_timestamp('2015-08-15 11:04:14.873066','null'),(select id from eg_module where name='Water Tax Management'));

INSERT INTO EG_ROLEACTION (actionid, roleid) select (select id from eg_action where url = '/digitalSignature/digitalSignaturePending-form'), (select id from eg_role where name in ('Property Administrator'));
INSERT INTO EG_ROLEACTION (actionid, roleid) select (select id from eg_action where url = '/digitalSignature/digitalSignaturePending-form'), (select id from eg_role where name in ('Property Verifier'));

update eg_wf_types set module =(select id from eg_module where name='Water Tax Management') where type='WaterConnectionDetails';

------Actions---
Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values 
(NEXTVAL('SEQ_EG_ACTION'),'digitalSignature-SignWorkOrder','/digitalSignature/waterTax/signWorkOrder', null,(select id from EG_MODULE where name = 'WaterTaxTransactions'),1,
null,'f','wtms',0,1,now(),1,now(),(select id from eg_module  where name = 'Water Tax Management'));

INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where url = '/digitalSignature/waterTax/signWorkOrder'), (select id from eg_role where name in ('Property Administrator'));
INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where url = '/digitalSignature/waterTax/signWorkOrder'), (select id from eg_role where name in ('Property Verifier'));