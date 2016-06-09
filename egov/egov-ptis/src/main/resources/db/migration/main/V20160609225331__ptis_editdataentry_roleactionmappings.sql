Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'EditDataEntry-Form','/search/searchProperty-commonForm.action', 'applicationType=Edit_Data_Entry',(select id from EG_MODULE where name = 'Existing property'),null,'Edit Data Entry ','f','ptis',0,1,now(),1,now(),(select id from eg_module  where name = 'Property Tax'));

INSERT INTO eg_roleaction(actionid, roleid) values ((select id from eg_action where name = 'EditDataEntry-Form'), 
(Select id from eg_role where name='Data Entry Operator'));

Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'PTIS-Modify Data Entry Screen','/create/createProperty-editDataEntryForm.action',null,(select id from EG_MODULE where name = 'Existing property'),null,'Edit Data Entry Screen','f','ptis',0,1,now(),1,now(),(select id from eg_module  where name = 'Property Tax'));

INSERT INTO eg_roleaction(actionid, roleid) values ((select id from eg_action where name = 'PTIS-Modify Data Entry Screen'), 
(Select id from eg_role where name='Data Entry Operator'));

Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'PTIS-Modify Data Entry Submit','/create/createProperty-updateDataEntry.action',null,(select id from EG_MODULE where name = 'Existing property'),null,'Modify Data Entry Submit ','f','ptis',0,1,now(),1,now(),(select id from eg_module  where name = 'Property Tax'));

INSERT INTO eg_roleaction(actionid, roleid) values ((select id from eg_action where name = 'PTIS-Modify Data Entry Submit'), 
(Select id from eg_role where name='Data Entry Operator'));


