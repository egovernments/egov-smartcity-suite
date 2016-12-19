INSERT into EG_ACTION 
(ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values 
(NEXTVAL('SEQ_EG_ACTION'),'taxCalculator','/create/createProperty-calculateTax.action', null,(select id from EG_MODULE where name = 'Property Tax'),1,
'taxCalculator','f','ptis',0,1,now(),1,now(),(select id from eg_module  where name = 'Property Tax'));

INSERT INTO eg_roleaction(actionid, roleid) values ((select id from eg_action where name = 'taxCalculator'), (Select id from eg_role where name='Property Verifier'));

INSERT INTO eg_roleaction(actionid, roleid) values ((select id from eg_action where name = 'taxCalculator'), (Select id from eg_role where name='ULB Operator'));

INSERT into EG_ACTION 
(ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values 
(NEXTVAL('SEQ_EG_ACTION'),'Modify property tax calculator','/modify/modifyProperty-calculateTax.action', null,(select id from EG_MODULE where name = 'Property Tax'),1,
'Modify property tax calculator','f','ptis',0,1,now(),1,now(),(select id from eg_module  where name = 'Property Tax'));

INSERT INTO eg_roleaction(actionid, roleid) values ((select id from eg_action where name = 'Modify property tax calculator'), (Select id from eg_role where name='Property Verifier'));

INSERT INTO eg_roleaction(actionid, roleid) values ((select id from eg_action where name = 'Modify property tax calculator'), (Select id from eg_role where name='ULB Operator'));
