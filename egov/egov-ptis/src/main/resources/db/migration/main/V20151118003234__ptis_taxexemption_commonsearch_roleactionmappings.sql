---eg_action---
Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'TaxExemption-Form','/search/searchProperty-commonForm.action', 'applicationType=Tax Exemption',(select id from EG_MODULE where name = 'Existing property'),null,'Tax Exemption','t','ptis',0,1,now(),1,now(),(select id from eg_module  where name = 'Property Tax'));

---eg_roleaction---
INSERT INTO eg_roleaction(actionid, roleid) values ((select id from eg_action where name = 'TaxExemption-Form'),(Select id from eg_role where name='ULB Operator'));

INSERT INTO eg_roleaction(actionid, roleid) values ((select id from eg_action where name = 'TaxExemption-Form'),(Select id from eg_role where name='CSC Operator'));