
INSERT into EG_ACTION 
(ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values 
(NEXTVAL('SEQ_EG_ACTION'),'Inventory Notice','/inventroy/searchform', null,(select id from EG_MODULE where name = 'Existing property'),6,
'Inventory Notice','t','ptis',0,1,now(),1,now(),(select id from eg_module  where name = 'Property Tax'));

INSERT into EG_ACTION 
(ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values 
(NEXTVAL('SEQ_EG_ACTION'),'Genearte Inventory Notice','/inventroy/generatenotice', null,(select id from EG_MODULE where name = 'Existing property'),6,
'Genearte Inventory Notice','f','ptis',0,1,now(),1,now(),(select id from eg_module  where name = 'Property Tax'));

INSERT INTO eg_roleaction(actionid, roleid) values ((select id from eg_action where name = 'Inventory Notice'), (Select id from eg_role where name='Property Approver'));

INSERT INTO eg_roleaction(actionid, roleid) values ((select id from eg_action where name = 'Genearte Inventory Notice'), (Select id from eg_role where name='Property Approver'));