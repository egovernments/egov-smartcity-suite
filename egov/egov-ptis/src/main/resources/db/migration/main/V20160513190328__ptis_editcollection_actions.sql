alter table egpt_basic_property add column eligible boolean default false;

Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) 
values (NEXTVAL('SEQ_EG_ACTION'),'EditCollection-editForm','/search/searchProperty-commonForm.action', 'applicationType=Edit_Collection', (select id from EG_MODULE where name = 'Existing property'),null,'Edit Collection',true,'ptis',0,1,now(),1,now(),(select id from eg_module  where name = 'Property Tax'));

Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) 
values (NEXTVAL('SEQ_EG_ACTION'),'EditCollection-Form','/editCollection/editForm', null, (select id from EG_MODULE where name = 'Existing property'),null,'Edit Collection',false,'ptis',0,1,now(),1,now(),(select id from eg_module  where name = 'Property Tax'));

Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) 
values (NEXTVAL('SEQ_EG_ACTION'),'EditCollection-update','/editCollection/update', null, (select id from EG_MODULE where name = 'Existing property'),null,'Edit Collection',false,'ptis',0,1,now(),1,now(),(select id from eg_module  where name = 'Property Tax'));


INSERT INTO eg_roleaction(actionid, roleid) values ((select id from eg_action where name = 'EditCollection-editForm'),(Select id from eg_role where name='Property Approver'));
INSERT INTO eg_roleaction(actionid, roleid) values ((select id from eg_action where name = 'EditCollection-Form'),(Select id from eg_role where name='Property Approver'));
INSERT INTO eg_roleaction(actionid, roleid) values ((select id from eg_action where name = 'EditCollection-update'),(Select id from eg_role where name='Property Approver'));
INSERT INTO eg_roleaction(actionid, roleid) values ((select id from eg_action where name = 'Assessment-commonSearch'),(Select id from eg_role where name='Property Approver'));
