
INSERT INTO EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values 
(NEXTVAL('SEQ_EG_ACTION'),'defaultersReport','/reports/defaultersReport-search.action', null,(select id from EG_MODULE where name = 'PTIS-Reports'),1,
'Defaulters Report','true','ptis',0,1,now(),1,now(),(select id from eg_module  where name = 'Property Tax'));


INSERT INTO EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values 
(NEXTVAL('SEQ_EG_ACTION'),'defaultersReportResult','/reports/defaultersReport-getDefaultersList.action', null,(select id from EG_MODULE where name = 'PTIS-Reports'),1,
'defaultersReportResult','false','ptis',0,1,now(),1,now(),(select id from eg_module  where name = 'Property Tax'));



INSERT INTO EG_ROLEACTION(actionid, roleid) values ((select id from eg_action where name = 'defaultersReport'), 
(Select id from eg_role where name='ULB Operator'));
INSERT INTO EG_ROLEACTION(actionid, roleid) values ((select id from eg_action where name = 'defaultersReport'), 
(Select id from eg_role where name='Super User'));
INSERT INTO EG_ROLEACTION(actionid, roleid) values ((select id from eg_action where name = 'defaultersReport'), 
(Select id from eg_role where name='Property Verifier'));
INSERT INTO EG_ROLEACTION(actionid, roleid) values ((select id from eg_action where name = 'defaultersReport'), 
(Select id from eg_role where name='Property Approver'));


INSERT INTO EG_ROLEACTION(actionid, roleid) values ((select id from eg_action where name = 'defaultersReportResult'), 
(Select id from eg_role where name='ULB Operator'));
INSERT INTO EG_ROLEACTION(actionid, roleid) values ((select id from eg_action where name = 'defaultersReportResult'), 
(Select id from eg_role where name='Super User'));
INSERT INTO EG_ROLEACTION(actionid, roleid) values ((select id from eg_action where name = 'defaultersReportResult'), 
(Select id from eg_role where name='Property Verifier'));
INSERT INTO EG_ROLEACTION(actionid, roleid) values ((select id from eg_action where name = 'defaultersReportResult'), 
(Select id from eg_role where name='Property Approver'));


