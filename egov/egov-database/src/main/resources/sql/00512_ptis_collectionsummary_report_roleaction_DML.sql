 INSERT INTO EG_ACTION(ID, NAME, URL, QUERYPARAMS, PARENTMODULE, ORDERNUMBER,DISPLAYNAME, ENABLED, CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION)
Values (nextval('SEQ_EG_ACTION'), 'Collection Summary Report Result','/reports/collectionSummaryReport-list.action', NULL, (SELECT ID FROM EG_MODULE WHERE NAME = 'PTIS-Reports'),1, 'Collection Summary Report Result',false,'ptis',0,1,now(),1,now(),(SELECT ID FROM EG_MODULE WHERE NAME = 'Property Tax'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where UPPER(name) = 'ULB OPERATOR'),(select id FROM eg_action  WHERE NAME = 'Collection Summary Report Result' and CONTEXTROOT='ptis'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where UPPER(name) = 'CSC OPERATOR'),(select id FROM eg_action  WHERE NAME = 'Collection Summary Report Result' and CONTEXTROOT='ptis'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where UPPER(name) = 'SUPER USER'),(select id FROM eg_action  WHERE NAME = 'Collection Summary Report Result' and CONTEXTROOT='ptis'));
 