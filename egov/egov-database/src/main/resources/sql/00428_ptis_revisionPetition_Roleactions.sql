INSERT INTO EG_ACTION(ID, NAME, URL, QUERYPARAMS, PARENTMODULE, ORDERNUMBER,DISPLAYNAME, ENABLED, CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE)
Values (nextval('SEQ_EG_ACTION'), 'PropTax Rev Petition print hearingnotice','/revPetition/revPetition-printHearingNotice.action', NULL, (SELECT ID FROM EG_MODULE WHERE NAME = 'Ptis Revision Petition'),1, 
'PropTax Rev Petition print hearingnotice',false,'ptis',0,1,now(),1,now());

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where name LIKE 'ULB Operator'),(select id FROM eg_action WHERE NAME = 'PropTax Rev Petition print hearingnotice' and CONTEXTROOT='ptis'));


INSERT INTO EG_ACTION(ID, NAME, URL, QUERYPARAMS, PARENTMODULE, ORDERNUMBER,DISPLAYNAME, ENABLED, CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE)
Values (nextval('SEQ_EG_ACTION'), 'PropTax Rev Petition print Endoresement','/revPetition/revPetition-printEnodresementNotice.action', NULL, (SELECT ID FROM EG_MODULE WHERE NAME = 'Ptis Revision Petition'),1, 
'PropTax Rev Petition print Endoresement',false,'ptis',0,1,now(),1,now());

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where name LIKE 'ULB Operator'),(select id FROM eg_action WHERE NAME = 'PropTax Rev Petition print Endoresement' and CONTEXTROOT='ptis'));
