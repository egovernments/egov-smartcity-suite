---- From common Search screen ----

Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'VacancyRemission-Form','/search/searchProperty-commonForm.action', 'applicationType=Vacancy Remission',(select id from EG_MODULE where name = 'Existing property'),null,'Vacancy Remission','t','ptis',0,1,now(),1,now(),(select id from eg_module  where name = 'Property Tax'));


INSERT INTO eg_roleaction(actionid, roleid) values ((select id from eg_action where name = 'VacancyRemission-Form'), 
(Select id from eg_role where name='ULB Operator'));

INSERT INTO eg_roleaction(actionid, roleid) values ((select id from eg_action where name = 'VacancyRemission-Form'), 
(Select id from eg_role where name='CSC Operator'));


