Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'ajaxLoadServiceByClassification','/receipts/ajaxReceiptCreate-ajaxLoadServiceByClassification.action',null,(select id from eg_module where name='Receipt Services'),1,'ajaxLoadServiceByClassification',false,'collection',0,1,now(),1,now(),(select id from eg_module where name='Collection'));

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='ULB Operator'),(select id from eg_action where name='ajaxLoadServiceByClassification'));
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Collection Operator'),(select id from eg_action where name='ajaxLoadServiceByClassification'));
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Remitter'),(select id from eg_action where name='ajaxLoadServiceByClassification'));
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Super User'),(select id from eg_action where name='ajaxLoadServiceByClassification'));

Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'ajaxLoadServiceByCategoryForMisc','/receipts/ajaxReceiptCreate-ajaxLoadServiceByCategoryForMisc.action',null,(select id from eg_module where name='Receipt Services'),1,'ajaxLoadServiceByCategoryForMisc',false,'collection',0,1,now(),1,now(),(select id from eg_module where name='Collection'));

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='ULB Operator'),(select id from eg_action where name='ajaxLoadServiceByCategoryForMisc'));
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Collection Operator'),(select id from eg_action where name='ajaxLoadServiceByCategoryForMisc'));
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Remitter'),(select id from eg_action where name='ajaxLoadServiceByCategoryForMisc'));
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Super User'),(select id from eg_action where name='ajaxLoadServiceByCategoryForMisc'));

