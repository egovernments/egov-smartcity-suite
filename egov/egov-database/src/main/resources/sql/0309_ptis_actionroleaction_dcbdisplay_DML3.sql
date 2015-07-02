--Enabling DCB view action
INSERT INTO EG_ACTION(ID, NAME, URL, QUERYPARAMS, PARENTMODULE, ORDERNUMBER,DISPLAYNAME, ENABLED, CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE)
Values (nextval('SEQ_EG_ACTION'), 'View DCB Property Display','/view/viewDCBProperty-displayPropInfo.action', NULL, (SELECT ID FROM EG_MODULE WHERE NAME = 'Existing property'),NULL, 'View DCB Property',false,'ptis',0,1,now(),1,now());

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where name = 'CSC Operator'),(select id FROM eg_action  WHERE NAME = 'View DCB Property Display' and CONTEXTROOT='ptis'));
