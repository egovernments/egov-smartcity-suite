Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'SearchAjax-PopulateData','/domain/commonTradeLicenseAjax-populateData.action',null,(select id from EG_MODULE where name = 'Trade License'),3,'SearchAjax-PopulateData','f','tl',0,1,now(),1,now(),(select id from eg_module  where name = 'Trade License'));

Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='SearchAjax-PopulateData'));

Insert into eg_roleaction values((select id from eg_role where name='TLCreator'),(select id from eg_action where name='SearchAjax-PopulateData'));

Insert into eg_roleaction values((select id from eg_role where name='TLApprover'),(select id from eg_action where name='SearchAjax-PopulateData'));
