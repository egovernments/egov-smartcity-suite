delete from eg_roleaction where actionid  in (select id from eg_action where name in ('CreateMiscReceipts', 'AjaxReceiptAccountDetailTypeService', 'AjaxFinAccDtlsByService', 'AjaxServiceByCategory', 'AjaxFinSubledgerByService', 'AjaxFinMiscByService'));

delete from eg_action where name in ('CreateMiscReceipts', 'AjaxReceiptAccountDetailTypeService', 'AjaxFinAccDtlsByService', 'AjaxServiceByCategory', 'AjaxFinSubledgerByService', 'AjaxFinMiscByService');

Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'CreateMiscReceipts','/receipts/receipt-newform.action','Receipt=Misc',(select id from eg_module where name='Receipt Services'),1,'Miscellaneous Receipt',true,'collection',0,1,current_timestamp,1,current_timestamp,(select id from eg_module where name='Collection'));

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='CSC Operator'),(select id from eg_action where name='CreateMiscReceipts'));

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='ULB Operator'),(select id from eg_action where name='CreateMiscReceipts'));

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Super User'),(select id from eg_action where name='CreateMiscReceipts'));

Insert into EG_ACTION ("id","name","url","queryparams","parentmodule","ordernumber","displayname","enabled","contextroot","version","createdby","createddate","lastmodifiedby","lastmodifieddate","application") values (nextval('seq_eg_action'),'AjaxReceiptAccountDetailTypeService','/receipts/ajaxReceiptCreate-getDetailTypeForService.action',null,(select ID from eg_module where NAME ='Receipt Services'),1,'AjaxReceiptAccountDetailTypeService','0','collection',0,1,current_timestamp,1,current_timestamp,(select id from eg_module where name='Collection' and parentmodule is null));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'ULB Operator') ,(select id FROM eg_action  WHERE name = 'AjaxReceiptAccountDetailTypeService' and contextroot='collection'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'CSC Operator') ,(select id FROM eg_action  WHERE name = 'AjaxReceiptAccountDetailTypeService' and contextroot='collection'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Super User') ,(select id FROM eg_action  WHERE name = 'AjaxReceiptAccountDetailTypeService' and contextroot='collection'));

Insert into EG_ACTION ("id","name","url","queryparams","parentmodule","ordernumber","displayname","enabled","contextroot","version","createdby","createddate","lastmodifiedby","lastmodifieddate","application") values (nextval('seq_eg_action'),'AjaxServiceByCategory','/receipts/ajaxReceiptCreate-ajaxLoadServiceByCategory.action',null,(select ID from eg_module where NAME ='Receipt Services'),1,'AjaxServiceByCategory','0','collection',0,1,current_timestamp,1,current_timestamp,(select id from eg_module where name='Collection' and parentmodule is null));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'ULB Operator') ,(select id FROM eg_action  WHERE name = 'AjaxServiceByCategory' and contextroot='collection'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'CSC Operator') ,(select id FROM eg_action  WHERE name = 'AjaxServiceByCategory' and contextroot='collection'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Super User') ,(select id FROM eg_action  WHERE name = 'AjaxServiceByCategory' and contextroot='collection'));

Insert into EG_ACTION ("id","name","url","queryparams","parentmodule","ordernumber","displayname","enabled","contextroot","version","createdby","createddate","lastmodifiedby","lastmodifieddate","application") values (nextval('seq_eg_action'),'AjaxFinMiscByService','/receipts/ajaxReceiptCreate-ajaxFinMiscDtlsByService.action',null,(select ID from eg_module where NAME ='Receipt Services'),1,'AjaxFinMiscByService','0','collection',0,1,current_timestamp,1,current_timestamp,(select id from eg_module where name='Collection' and parentmodule is null));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'ULB Operator') ,(select id FROM eg_action  WHERE name = 'AjaxFinMiscByService' and contextroot='collection'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'CSC Operator') ,(select id FROM eg_action  WHERE name = 'AjaxFinMiscByService' and contextroot='collection'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Super User') ,(select id FROM eg_action  WHERE name = 'AjaxFinMiscByService' and contextroot='collection'));

Insert into EG_ACTION ("id","name","url","queryparams","parentmodule","ordernumber","displayname","enabled","contextroot","version","createdby","createddate","lastmodifiedby","lastmodifieddate","application") values (nextval('seq_eg_action'),'AjaxFinAccDtlsByService','/receipts/ajaxReceiptCreate-ajaxFinAccDtlsByService.action',null,(select ID from eg_module where NAME ='Receipt Services'),1,'AjaxFinAccDtlsByService','0','collection',0,1,current_timestamp,1,current_timestamp,(select id from eg_module where name='Collection' and parentmodule is null));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'ULB Operator') ,(select id FROM eg_action  WHERE name = 'AjaxFinAccDtlsByService' and contextroot='collection'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'CSC Operator') ,(select id FROM eg_action  WHERE name = 'AjaxFinAccDtlsByService' and contextroot='collection'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Super User') ,(select id FROM eg_action  WHERE name = 'AjaxFinAccDtlsByService' and contextroot='collection'));


Insert into EG_ACTION ("id","name","url","queryparams","parentmodule","ordernumber","displayname","enabled","contextroot","version","createdby","createddate","lastmodifiedby","lastmodifieddate","application") values (nextval('seq_eg_action'),'AjaxFinSubledgerByService','/receipts/ajaxReceiptCreate-ajaxFinSubledgerByService.action',null,(select ID from eg_module where NAME ='Receipt Services'),1,'AjaxFinSubledgerByService','0','collection',0,1,current_timestamp,1,current_timestamp,(select id from eg_module where name='Collection' and parentmodule is null));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'ULB Operator') ,(select id FROM eg_action  WHERE name = 'AjaxFinSubledgerByService' and contextroot='collection'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'CSC Operator') ,(select id FROM eg_action  WHERE name = 'AjaxFinSubledgerByService' and contextroot='collection'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Super User') ,(select id FROM eg_action  WHERE name = 'AjaxFinSubledgerByService' and contextroot='collection'));


