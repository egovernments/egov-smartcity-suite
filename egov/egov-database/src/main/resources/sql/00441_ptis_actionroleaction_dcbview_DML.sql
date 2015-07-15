--Enabling DCB view action
INSERT INTO EG_ACTION(ID, NAME, URL, QUERYPARAMS, PARENTMODULE, ORDERNUMBER,DISPLAYNAME, ENABLED, CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,application)
Values (nextval('SEQ_EG_ACTION'), 'View DCB Property','/view/viewDCBProperty-viewForm.action', NULL, (SELECT ID FROM EG_MODULE WHERE NAME = 'Existing property'),NULL, 'View DCB Property',false,'ptis',0,1,now(),1,now(),(select id from eg_module where "name" ='Property Tax'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where name = 'CSC Operator'),(select id FROM eg_action  WHERE NAME = 'View DCB Property' and CONTEXTROOT='ptis'));
