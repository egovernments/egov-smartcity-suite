INSERT INTO EG_ACTION(ID, NAME, URL, QUERYPARAMS, PARENTMODULE, ORDERNUMBER,DISPLAYNAME, ENABLED, CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE)
Values (nextval('SEQ_EG_ACTION'), 'createPropertyAckPrint','/create/createProperty-printAck.action', NULL, (SELECT ID FROM EG_MODULE WHERE NAME = 'New Property'),4, 'createPropertyAckPrint',false,'ptis',0,1,now(),1,now());

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where name LIKE 'ULB Operator'),(select id FROM eg_action WHERE NAME = 'createPropertyAckPrint' and CONTEXTROOT='ptis'));



--rollback delete from EG_ROLEACTION where actionid=(select id from eg_action where name='createPropertyAckPrint');
--rollback delete from EG_ACTION where name='createPropertyAckPrint';



