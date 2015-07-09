
INSERT INTO EG_ACTION(ID, NAME, URL, QUERYPARAMS, PARENTMODULE, ORDERNUMBER,DISPLAYNAME, ENABLED, CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE)
Values (nextval('SEQ_EG_ACTION'), 'Populate Designation For Revision Petition','/common/ajaxCommon-populateDesignationsByDeptForRevisionPetition.action', NULL, (SELECT ID FROM EG_MODULE WHERE NAME = 'Ptis Revision Petition'),NULL, 'Populate Designation For Revision Petition',false,'ptis',0,1,now(),1,now());

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where name = 'CSC Operator'),(select id FROM eg_action  WHERE NAME = 'Populate Designation For Revision Petition' and CONTEXTROOT='ptis'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where name = 'ULB Operator'),(select id FROM eg_action  WHERE NAME = 'Populate Designation For Revision Petition' and CONTEXTROOT='ptis'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where name = 'Property Administrator'),(select id FROM eg_action  WHERE NAME = 'Populate Designation For Revision Petition' and CONTEXTROOT='ptis'));

update eg_action set url='/revPetition/revPetition.action',name='Property Tax Rev Petition Action',displayname='Property Tax Rev Petition Action' where  name='PropTax objectionAction';

update eg_action set url='/revPetition/revPetition-view.action',name='Property Tax Rev Petition View',displayname='Property Tax Rev Petition View' where  name='PropTax objectionView';

update eg_action set url='/revPetition/revPetition-newForm.action',name='Property Tax Rev Petition New',displayname='Property Tax Rev Petition New' where  name='PropTax objectionNew';


INSERT INTO EG_ACTION(ID, NAME, URL, QUERYPARAMS, PARENTMODULE, ORDERNUMBER,DISPLAYNAME, ENABLED, CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE)
Values (nextval('SEQ_EG_ACTION'), 'PropTax Rev Petition Add hearing','/revPetition/revPetition-addHearingDate.action', NULL, (SELECT ID FROM EG_MODULE WHERE NAME = 'Ptis Revision Petition'),1, 'PropTax Rev Petition Add hearing',false,'ptis',0,1,now(),1,now());

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where name LIKE 'Property Administrator'),(select id FROM eg_action WHERE NAME = 'PropTax Rev Petition Add hearing' and CONTEXTROOT='ptis'));

INSERT INTO EG_ACTION(ID, NAME, URL, QUERYPARAMS, PARENTMODULE, ORDERNUMBER,DISPLAYNAME, ENABLED, CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE)
Values (nextval('SEQ_EG_ACTION'), 'PropTax Rev Petition generate hearingnotice','/revPetition/revPetition-generateHearingNotice', NULL, (SELECT ID FROM EG_MODULE WHERE NAME = 'PropTax Rev Petition generate hearingnotice'),1, 'PropTax Rev Petition Add hearing',false,'ptis',0,1,now(),1,now());

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where name LIKE 'ULB Operator'),(select id FROM eg_action WHERE NAME = 'PropTax Rev Petition generate hearingnotice' and CONTEXTROOT='ptis'));


INSERT INTO EG_ACTION(ID, NAME, URL, QUERYPARAMS, PARENTMODULE, ORDERNUMBER,DISPLAYNAME, ENABLED, CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE)
Values (nextval('SEQ_EG_ACTION'), 'PropTax Rev Petition record hearing','/revPetition/revPetition-recordHearingDetails', NULL, (SELECT ID FROM EG_MODULE WHERE NAME = 'PropTax Rev Petition generate hearingnotice'),1, 'PropTax Rev Petition record hearing',false,'ptis',0,1,now(),1,now());

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where name LIKE 'Property Verifier'),(select id FROM eg_action WHERE NAME = 'PropTax Rev Petition record hearing' and CONTEXTROOT='ptis'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where name LIKE 'Property Administrator'),(select id FROM eg_action WHERE NAME = 'Property Tax Rev Petition View' and CONTEXTROOT='ptis'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where name LIKE 'Property Administrator'),(select id FROM eg_action WHERE NAME = 'Property Tax Rev Petition Action' and CONTEXTROOT='ptis'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where name LIKE 'Property Verifier'),(select id FROM eg_action WHERE NAME = 'Property Tax Rev Petition View' and CONTEXTROOT='ptis'));

INSERT INTO EG_ACTION(ID, NAME, URL, QUERYPARAMS, PARENTMODULE, ORDERNUMBER,DISPLAYNAME, ENABLED, CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE)
Values (nextval('SEQ_EG_ACTION'), 'PropTax Rev Petition record inspection','/revPetition/revPetition-recordInspectionDetails', NULL, (SELECT ID FROM EG_MODULE WHERE NAME = 'PropTax Rev Petition generate hearingnotice'),1, 'PropTax Rev Petition record inspection',false,'ptis',0,1,now(),1,now());

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where name LIKE 'Property Verifier'),(select id FROM eg_action WHERE NAME = 'PropTax Rev Petition record inspection' and CONTEXTROOT='ptis'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where name = 'Property Verifier'),(select id FROM eg_action  WHERE NAME = 'Populate Designation For Revision Petition' and CONTEXTROOT='ptis'));


INSERT INTO EG_ACTION(ID, NAME, URL, QUERYPARAMS, PARENTMODULE, ORDERNUMBER,DISPLAYNAME, ENABLED, CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE)
Values (nextval('SEQ_EG_ACTION'), 'PropTax Rev Petition Outcome','/revPetition/revPetition-recordObjectionOutcome', NULL, (SELECT ID FROM EG_MODULE WHERE NAME = 'PropTax Rev Petition generate hearingnotice'),1, 'PropTax Rev Petition Outcome',false,'ptis',0,1,now(),1,now());

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where name LIKE 'Property Verifier'),(select id FROM eg_action WHERE NAME = 'PropTax Rev Petition Outcome' and CONTEXTROOT='ptis'));

INSERT INTO EG_ACTION(ID, NAME, URL, QUERYPARAMS, PARENTMODULE, ORDERNUMBER,DISPLAYNAME, ENABLED, CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE)
Values (nextval('SEQ_EG_ACTION'), 'PropTax Rev Petition endoresement notice','/revPetition/revPetition-generateEnodresementNotice', NULL, (SELECT ID FROM EG_MODULE WHERE NAME = 'PropTax Rev Petition generate hearingnotice'),1, 'PropTax Rev Petition endoresement notice',false,'ptis',0,1,now(),1,now());

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where name LIKE 'ULB Operator'),(select id FROM eg_action WHERE NAME = 'PropTax Rev Petition endoresement notice' and CONTEXTROOT='ptis'));


 
 