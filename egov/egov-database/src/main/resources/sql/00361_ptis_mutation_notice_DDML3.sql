--Enabling DCB view action
INSERT INTO EG_ACTION(ID, NAME, URL, QUERYPARAMS, PARENTMODULE, ORDERNUMBER,DISPLAYNAME, ENABLED, CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE)
Values (nextval('SEQ_EG_ACTION'), 'Notice Transfer Property','/property/transfer/printNotice.action', NULL, (SELECT ID FROM EG_MODULE WHERE NAME = 'Existing property'),NULL, 'Acknowledgement Transfer Property',false,'ptis',0,1,now(),1,now());

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where name = 'CSC Operator'),(select id FROM eg_action  WHERE NAME = 'Notice Transfer Property' and CONTEXTROOT='ptis'));
