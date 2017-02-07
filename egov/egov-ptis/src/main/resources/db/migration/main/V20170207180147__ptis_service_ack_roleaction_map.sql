
INSERT into EG_ACTION 
(ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values 
(NEXTVAL('SEQ_EG_ACTION'),'Exemption Ack','/exemption/printAck', null,(select id from EG_MODULE where name = 'Existing property'),6,
'Exemption Ack','f','ptis',0,1,now(),1,now(),(select id from eg_module  where name = 'Property Tax'));

INSERT INTO eg_roleaction(actionid, roleid) values ((select id from eg_action where name = 'Exemption Ack'), (Select id from eg_role where name='ULB Operator'));


INSERT into EG_ACTION 
(ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values 
(NEXTVAL('SEQ_EG_ACTION'),'Vacancy Remission Ack','/vacancyremission/printAck', null,(select id from EG_MODULE where name = 'Existing property'),6,
'Vacancy Remission Ack','f','ptis',0,1,now(),1,now(),(select id from eg_module  where name = 'Property Tax'));

INSERT INTO eg_roleaction(actionid, roleid) values ((select id from eg_action where name = 'Vacancy Remission Ack'), (Select id from eg_role where name='ULB Operator'));


INSERT into EG_ACTION 
(ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values 
(NEXTVAL('SEQ_EG_ACTION'),'RP GRP Ack','/revPetition/revPetition-printAck.action', null,(select id from EG_MODULE where name = 'Existing property'),6,
'RP GRP Ack','f','ptis',0,1,now(),1,now(),(select id from eg_module  where name = 'Property Tax'));

INSERT INTO eg_roleaction(actionid, roleid) values ((select id from eg_action where name = 'RP GRP Ack'), (Select id from eg_role where name='ULB Operator'));


INSERT into EG_ACTION 
(ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values 
(NEXTVAL('SEQ_EG_ACTION'),'Amalgamation Ack','/amalgamation/amalgamation-printAck.action', null,(select id from EG_MODULE where name = 'Existing property'),6,
'Amalgamation Ack','f','ptis',0,1,now(),1,now(),(select id from eg_module  where name = 'Property Tax'));

INSERT INTO eg_roleaction(actionid, roleid) values ((select id from eg_action where name = 'Amalgamation Ack'), (Select id from eg_role where name='ULB Operator'));


INSERT into EG_ACTION 
(ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values 
(NEXTVAL('SEQ_EG_ACTION'),'Demolition Ack','/demolition/ack/printAck', null,(select id from EG_MODULE where name = 'Existing property'),6,
'Demolition Ack','f','ptis',0,1,now(),1,now(),(select id from eg_module  where name = 'Property Tax'));

INSERT INTO eg_roleaction(actionid, roleid) values ((select id from eg_action where name = 'Demolition Ack'), (Select id from eg_role where name='ULB Operator'));



insert into egpt_application_type values(nextval('seq_egpt_application_type'),'GENERAL_REVISION_PETETION','GENERAL REVISION PETETION',30,'GENERAL REVISION PETETION',now(),null,1,null,null);
insert into egpt_application_type values(nextval('seq_egpt_application_type'),'AMALGAMATION','AMALGAMATION',30,'AMALGAMATION',now(),null,1,null,null);
insert into egpt_application_type values(nextval('seq_egpt_application_type'),'DEMOLITION','DEMOLITION',30,'DEMOLITION',now(),null,1,null,null);
insert into egpt_application_type values(nextval('seq_egpt_application_type'),'TAX_EXEMPTION','TAX EXEMPTION',30,'TAX EXEMPTION',now(),null,1,null,null);