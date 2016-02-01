Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'Ajax-FeeTypeBySubCategory','/domain/commonAjax-ajaxPopulateFeeType.action',null,(select id from EG_MODULE where name = 'Trade License'),2,'Ajax-FeeTypeBySubCategory','f','tl',0,1,now(),1,now(),(select id from eg_module  where name = 'Trade License'));

Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Ajax-FeeTypeBySubCategory'));


Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'Ajax-UnitOfMeasurementBySubCategory','/domain/commonAjax-ajaxPopulateUom.action',null,(select id from EG_MODULE where name = 'Trade License'),2,'Ajax-UnitOfMeasurementBySubCategory','f','tl',0,1,now(),1,now(),(select id from eg_module  where name = 'Trade License'));

Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Ajax-UnitOfMeasurementBySubCategory'));
