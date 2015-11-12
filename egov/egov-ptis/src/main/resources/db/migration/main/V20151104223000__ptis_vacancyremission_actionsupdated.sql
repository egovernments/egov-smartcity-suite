update eg_action set name = 'vacancyRemissionCreate', url = '/vacancyremission/create/', DISPLAYNAME='vacancyRemissionCreate' where name = 'vacancyRemission';


INSERT into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values 
(NEXTVAL('SEQ_EG_ACTION'),'vacancyRemissionUpdate','/vacancyremission/update/', null,(select id from EG_MODULE where name = 'Existing property'),1,
'vacancyRemissionUpdate','f','ptis',0,1,now(),1,now(),(select id from eg_module  where name = 'Property Tax'));
----------------------- Monthly update screen ---------------
INSERT into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values 
(NEXTVAL('SEQ_EG_ACTION'),'remissionMonthlyUpdateForm','/vacancyremission/monthlyupdate/', null,(select id from EG_MODULE where name = 'Existing property'),1,
'remissionMonthlyUpdateForm','f','ptis',0,1,now(),1,now(),(select id from eg_module  where name = 'Property Tax'));


INSERT INTO eg_roleaction(actionid, roleid) values ((select id from eg_action where name = 'vacancyRemissionUpdate'), 
(Select id from eg_role where name='Property Verifier'));
INSERT INTO eg_roleaction(actionid, roleid) values ((select id from eg_action where name = 'vacancyRemissionUpdate'), 
(Select id from eg_role where name='Property Approver'));
INSERT INTO eg_roleaction(actionid, roleid) values ((select id from eg_action where name = 'vacancyRemissionUpdate'), 
(Select id from eg_role where name='ULB Operator'));
----------------------- Monthly update screen ---------------
INSERT INTO eg_roleaction(actionid, roleid) values ((select id from eg_action where name = 'remissionMonthlyUpdateForm'), 
(Select id from eg_role where name='Property Verifier'));


