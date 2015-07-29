INSERT INTO EG_ACTION(ID, NAME, URL, QUERYPARAMS, PARENTMODULE, ORDERNUMBER,DISPLAYNAME, ENABLED, CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION)
Values (nextval('SEQ_EG_ACTION'), 'Zone Wise Collection Report','/reports/collectionSummaryReport-zoneWise.action', NULL, (SELECT ID FROM EG_MODULE WHERE NAME = 'PTIS-Reports'),0, 'Revenue Zone Wise Collection Report',true,'ptis',0,1,now(),1,now(),(SELECT ID FROM EG_MODULE WHERE NAME = 'Property Tax'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where UPPER(name) = 'ULB OPERATOR'),(select id FROM eg_action  WHERE NAME = 'Zone Wise Collection Report' and CONTEXTROOT='ptis'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where UPPER(name) = 'CSC OPERATOR'),(select id FROM eg_action  WHERE NAME = 'Zone Wise Collection Report' and CONTEXTROOT='ptis'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where UPPER(name) = 'SUPER USER'),(select id FROM eg_action  WHERE NAME = 'Zone Wise Collection Report' and CONTEXTROOT='ptis'));


INSERT INTO EG_ACTION(ID, NAME, URL, QUERYPARAMS, PARENTMODULE, ORDERNUMBER,DISPLAYNAME, ENABLED, CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION)
Values (nextval('SEQ_EG_ACTION'), 'Ward Wise Collection Report','/reports/collectionSummaryReport-wardWise.action', NULL, (SELECT ID FROM EG_MODULE WHERE NAME = 'PTIS-Reports'),0, 'Revenue Ward Wise Collection Report',true,'ptis',0,1,now(),1,now(),(SELECT ID FROM EG_MODULE WHERE NAME = 'Property Tax'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where UPPER(name) = 'ULB OPERATOR'),(select id FROM eg_action  WHERE NAME = 'Ward Wise Collection Report' and CONTEXTROOT='ptis'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where UPPER(name) = 'CSC OPERATOR'),(select id FROM eg_action  WHERE NAME = 'Ward Wise Collection Report' and CONTEXTROOT='ptis'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where UPPER(name) = 'SUPER USER'),(select id FROM eg_action  WHERE NAME = 'Ward Wise Collection Report' and CONTEXTROOT='ptis'));


INSERT INTO EG_ACTION(ID, NAME, URL, QUERYPARAMS, PARENTMODULE, ORDERNUMBER,DISPLAYNAME, ENABLED, CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION)
Values (nextval('SEQ_EG_ACTION'), 'Block Wise Collection Report','/reports/collectionSummaryReport-blockWise.action', NULL, (SELECT ID FROM EG_MODULE WHERE NAME = 'PTIS-Reports'),0, 'Revenue Block Wise Collection Report',true,'ptis',0,1,now(),1,now(),(SELECT ID FROM EG_MODULE WHERE NAME = 'Property Tax'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where UPPER(name) = 'ULB OPERATOR'),(select id FROM eg_action  WHERE NAME = 'Block Wise Collection Report' and CONTEXTROOT='ptis'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where UPPER(name) = 'CSC OPERATOR'),(select id FROM eg_action  WHERE NAME = 'Block Wise Collection Report' and CONTEXTROOT='ptis'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where UPPER(name) = 'SUPER USER'),(select id FROM eg_action  WHERE NAME = 'Block Wise Collection Report' and CONTEXTROOT='ptis'));


INSERT INTO EG_ACTION(ID, NAME, URL, QUERYPARAMS, PARENTMODULE, ORDERNUMBER,DISPLAYNAME, ENABLED, CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION)
Values (nextval('SEQ_EG_ACTION'), 'Locality Wise Collection Report','/reports/collectionSummaryReport-localityWise.action', NULL, (SELECT ID FROM EG_MODULE WHERE NAME = 'PTIS-Reports'),0, 'Revenue Locality Wise Collection Report',true,'ptis',0,1,now(),1,now(),(SELECT ID FROM EG_MODULE WHERE NAME = 'Property Tax'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where UPPER(name) = 'ULB OPERATOR'),(select id FROM eg_action  WHERE NAME = 'Locality Wise Collection Report' and CONTEXTROOT='ptis'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where UPPER(name) = 'CSC OPERATOR'),(select id FROM eg_action  WHERE NAME = 'Locality Wise Collection Report' and CONTEXTROOT='ptis'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where UPPER(name) = 'SUPER USER'),(select id FROM eg_action  WHERE NAME = 'Locality Wise Collection Report' and CONTEXTROOT='ptis'));

