------ESTIMATION NOTICE ROLEACTION MAP--------
INSERT INTO EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'SewerageTaxEstimationNotice','/application/estimationnotice',null,(select id from EG_MODULE where name = 'Sewerage Tax Management'),1,'Sewearge Estimate Notice','false','stms',0,1,now(),1,now(),(select id from eg_module where name = 'Sewerage Tax Management'));
INSERT INTO EG_ROLEACTION (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='SewerageTaxEstimationNotice' and contextroot = 'stms'));

------WORKORDER NOTICE ROLEACTION MAP--------
INSERT INTO EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'SewerageTaxWorkOrderNotice','/application/workordernotice',null,(select id from EG_MODULE where name = 'Sewerage Tax Management'),1,'Sewearge Work Order Notice','false','stms',0,1,now(),1,now(),(select id from eg_module where name = 'Sewerage Tax Management'));
INSERT INTO EG_ROLEACTION (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='SewerageTaxWorkOrderNotice' and contextroot = 'stms'));

--ACKNOWLEDGEMENT ROLEACTION MAP----
INSERT INTO EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'NewSewerageConnectionAck','/transactions/newconnectionack',null,(select id from EG_MODULE where name = 'Sewerage Tax Management'),1,'Sewearge New Connection Acknowledgement','false','stms',0,1,now(),1,now(),(select id from eg_module where name = 'Sewerage Tax Management'));
INSERT INTO EG_ROLEACTION (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='NewSewerageConnectionAck' and contextroot = 'stms'));

