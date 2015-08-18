-- for Menu tree link
INSERT INTO EG_ACTION(ID, NAME, URL, QUERYPARAMS, PARENTMODULE, ORDERNUMBER,DISPLAYNAME, ENABLED, CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION)
Values (nextval('SEQ_EG_ACTION'), 'Arrears Register Report','/reports/arrearRegisterReport-index.action', NULL, (SELECT ID FROM EG_MODULE WHERE NAME = 'PTIS-Reports'),0, 'Arrears Register Report',true,'ptis',0,1,now(),1,now(),(SELECT ID FROM EG_MODULE WHERE NAME = 'Property Tax'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where UPPER(name) = 'ULB OPERATOR'),(select id FROM eg_action  WHERE NAME = 'Arrears Register Report' and CONTEXTROOT='ptis'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where UPPER(name) = 'CSC OPERATOR'),(select id FROM eg_action  WHERE NAME = 'Arrears Register Report' and CONTEXTROOT='ptis'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where UPPER(name) = 'SUPER USER'),(select id FROM eg_action  WHERE NAME = 'Arrears Register Report' and CONTEXTROOT='ptis'));

-- for arrear register report search
INSERT INTO EG_ACTION(ID, NAME, URL, QUERYPARAMS, PARENTMODULE, ORDERNUMBER,DISPLAYNAME, ENABLED, CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION)
Values (nextval('SEQ_EG_ACTION'), 'GenerateArrearRegisterReport','/reports/arrearRegisterReport-generateArrearReport.action', NULL, (SELECT ID FROM EG_MODULE WHERE NAME = 'PTIS-Reports'),0, 'GenerateArrearRegisterReport',false,'ptis',0,1,now(),1,now(),(SELECT ID FROM EG_MODULE WHERE NAME = 'Property Tax'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where UPPER(name) = 'ULB OPERATOR'),(select id FROM eg_action  WHERE NAME = 'GenerateArrearRegisterReport' and CONTEXTROOT='ptis'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where UPPER(name) = 'CSC OPERATOR'),(select id FROM eg_action  WHERE NAME = 'GenerateArrearRegisterReport' and CONTEXTROOT='ptis'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where UPPER(name) = 'SUPER USER'),(select id FROM eg_action  WHERE NAME = 'GenerateArrearRegisterReport' and CONTEXTROOT='ptis'));

-- to load ward drilldown through ajaxcall
INSERT INTO EG_ACTION(ID, NAME, URL, QUERYPARAMS, PARENTMODULE, ORDERNUMBER,DISPLAYNAME, ENABLED, CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION)
Values (nextval('SEQ_EG_ACTION'), 'ajaxLoadBoundaryWard','/common/ajaxCommon-wardByZone.action', NULL, (SELECT ID FROM EG_MODULE WHERE NAME = 'PTIS-Reports'),0, 'ajaxLoadBoundary',false,'ptis',0,1,now(),1,now(),(SELECT ID FROM EG_MODULE WHERE NAME = 'Property Tax'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where UPPER(name) = 'ULB OPERATOR'),(select id FROM eg_action  WHERE NAME = 'ajaxLoadBoundaryWard' and CONTEXTROOT='ptis'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where UPPER(name) = 'CSC OPERATOR'),(select id FROM eg_action  WHERE NAME = 'ajaxLoadBoundaryWard' and CONTEXTROOT='ptis'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where UPPER(name) = 'SUPER USER'),(select id FROM eg_action  WHERE NAME = 'ajaxLoadBoundaryWard' and CONTEXTROOT='ptis'));


-- to load zone drilldown through ajaxcall
INSERT INTO EG_ACTION(ID, NAME, URL, QUERYPARAMS, PARENTMODULE, ORDERNUMBER,DISPLAYNAME, ENABLED, CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION)
Values (nextval('SEQ_EG_ACTION'), 'ajaxLoadBoundaryBlock','/common/ajaxCommon-areaByWard.action', NULL, (SELECT ID FROM EG_MODULE WHERE NAME = 'PTIS-Reports'),0, 'ajaxLoadBoundaryBlock',false,'ptis',0,1,now(),1,now(),(SELECT ID FROM EG_MODULE WHERE NAME = 'Property Tax'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where UPPER(name) = 'ULB OPERATOR'),(select id FROM eg_action  WHERE NAME = 'ajaxLoadBoundaryBlock' and CONTEXTROOT='ptis'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where UPPER(name) = 'CSC OPERATOR'),(select id FROM eg_action  WHERE NAME = 'ajaxLoadBoundaryBlock' and CONTEXTROOT='ptis'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where UPPER(name) = 'SUPER USER'),(select id FROM eg_action  WHERE NAME = 'ajaxLoadBoundaryBlock' and CONTEXTROOT='ptis'));

/*
--- Down script -----

Delete from eg_roleaction where actionid in (select id from eg_action where name in ('ajaxLoadBoundaryBlock','ajaxLoadBoundaryWard','GenerateArrearRegisterReport','Arrears Register Report') and contextroot='ptis');

Delete from eg_action where name in ('ajaxLoadBoundaryBlock','ajaxLoadBoundaryWard','GenerateArrearRegisterReport','Arrears Register Report')
and contextroot='ptis';

*/

