-----Actions---
Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'Search Property By Door No','/search/searchProperty-srchByDoorNo.action', 
null,(select id from EG_MODULE where name = 'Existing property'),null,'Search Property By Door No',false,'ptis',0,1,now(),1,now(),(select id from eg_module  where name = 'Property Tax'));

Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'Search Property By Mobile No','/search/searchProperty-srchByMobileNumber.action', 
null,(select id from EG_MODULE where name = 'Existing property'),null,'Search Property By Mobile No',false,'ptis',0,1,now(),1,now(),(select id from eg_module  where name = 'Property Tax'));

-----Roleaction Mappings----
INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Search Property By Door No'), id from eg_role where name in ('CSC Operator','Super User','ULB Operator','Property Administrator','Property Approver','Property Verifier');

INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Search Property By Mobile No'), id from eg_role where name in ('CSC Operator','Super User','ULB Operator','Property Administrator','Property Approver','Property Verifier');