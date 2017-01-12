
INSERT into EG_ACTION 
(ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values 
(NEXTVAL('SEQ_EG_ACTION'),'Valuation Certificate','/valuation/searchform', null,(select id from EG_MODULE where name = 'PTIS-Reports'),6,
'Valuation Certificate','t','ptis',0,1,now(),1,now(),(select id from eg_module  where name = 'Property Tax'));

INSERT into EG_ACTION 
(ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values 
(NEXTVAL('SEQ_EG_ACTION'),'Genearte Valuation Certificate','/valuation/generatenotice', null,(select id from EG_MODULE where name = 'PTIS-Reports'),6,
'Genearte Valuation Certificate','f','ptis',0,1,now(),1,now(),(select id from eg_module  where name = 'Property Tax'));

INSERT INTO eg_roleaction(actionid, roleid) values ((select id from eg_action where name = 'Valuation Certificate'), (Select id from eg_role where name='Property Approver'));

INSERT INTO eg_roleaction(actionid, roleid) values ((select id from eg_action where name = 'Genearte Valuation Certificate'), (Select id from eg_role where name='Property Approver'));