-----Actions---
Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'Generate Bulk Notices','/notice/propertyTaxNotice-generateBulkNotice', 
null,(select id from EG_MODULE where name = 'New Property'),4,'Generate Bulk Notices','false','ptis',0,1,now(),1,now(),(select id from eg_module  where name = 'Property Tax'));

-----Roleaction Mappings----
INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Generate Bulk Notices'), id from eg_role where name in ('Property Verifier');
