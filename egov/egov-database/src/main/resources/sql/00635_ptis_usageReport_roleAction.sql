-- for menu tree link
INSERT INTO EG_ACTION(ID, NAME, URL, QUERYPARAMS, PARENTMODULE, ORDERNUMBER,DISPLAYNAME, ENABLED, CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION)
Values (nextval('SEQ_EG_ACTION'), 'Usage Wise Collection Summary Report','/reports/collectionSummaryReport-usageWise.action', NULL, (SELECT ID FROM EG_MODULE WHERE NAME = 'PTIS-Reports'),0, 'Usage Wise Collection Summary Report',true,'ptis',0,1,now(),1,now(),(SELECT ID FROM EG_MODULE WHERE NAME = 'Property Tax'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where UPPER(name) = 'ULB OPERATOR'),(select id FROM eg_action  WHERE NAME = 'Usage Wise Collection Summary Report' and CONTEXTROOT='ptis'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where UPPER(name) = 'CSC OPERATOR'),(select id FROM eg_action  WHERE NAME = 'Usage Wise Collection Summary Report' and CONTEXTROOT='ptis'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where UPPER(name) = 'SUPER USER'),(select id FROM eg_action  WHERE NAME = 'Usage Wise Collection Summary Report' and CONTEXTROOT='ptis'));


/*
--- Down script -----

Delete from eg_roleaction where actionid in (select id from eg_action where name in ('Usage Wise Collection Summary Report') and contextroot='ptis');

Delete from eg_action where name in ('Usage Wise Collection Summary Report')
and contextroot='ptis';

*/
