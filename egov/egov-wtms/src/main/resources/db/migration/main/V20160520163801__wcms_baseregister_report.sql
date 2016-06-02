 --------------------------------START--------------------------------------

INSERT into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'BaseRegister Report','/report/baseRegister', null,(select id from EG_MODULE where name = 'WaterTaxReports'),null, 'Base Register Report','t','wtms',0,1,now(),1,now(),(select id from eg_module  where name = 'Water Tax Management'));

Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'BaseRegister Report result','/report/baseRegister/result', null,(select id from EG_MODULE where name = 'WaterTaxReports'),null,'Base Register Report result',false,'wtms',0,1,now(),1,now(),(select id from eg_module  where name = 'Water Tax Management'));

INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'BaseRegister Report'and contextroot = 'wtms'));

INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'BaseRegister Report result'and contextroot = 'wtms'));

INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Water Tax Report Viewer'), (select id from eg_action where name = 'BaseRegister Report'and contextroot = 'wtms'));

INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Water Tax Report Viewer'), (select id from eg_action where name = 'BaseRegister Report result'and contextroot = 'wtms'));


  ----------------------------------END------------------------------------------

