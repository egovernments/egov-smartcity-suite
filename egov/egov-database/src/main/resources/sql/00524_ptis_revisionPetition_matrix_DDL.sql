INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name = 'CSC Operator'),(select id FROM eg_action  WHERE NAME = 'Property Tax Rev Petition New' and CONTEXTROOT='ptis'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name = 'CSC Operator'),(select id FROM eg_action  WHERE NAME = 'Property Tax Rev Petition Action' and CONTEXTROOT='ptis'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name = 'CSC Operator'),(select id FROM eg_action  WHERE NAME = 'AjaxDesignationDropdown'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name = 'CSC Operator'),(select id FROM eg_action  WHERE NAME = 'AjaxApproverDropdown'));

INSERT INTO EG_ACTION(ID, NAME, URL, QUERYPARAMS, PARENTMODULE, ORDERNUMBER,DISPLAYNAME, ENABLED, CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,application) Values (nextval('SEQ_EG_ACTION'), 'PropTax Rev Petition rejection','/revPetition/revPetition-reject.action', NULL, (SELECT ID FROM EG_MODULE WHERE NAME = 'Ptis Revision Petition'),1, 'PropTax Rev Petition rejection',false,'ptis',0,1,now(),1,now(),(select id from eg_module where name='Property Tax' and parentmodule is null));

INSERT INTO EG_ACTION(ID, NAME, URL, QUERYPARAMS, PARENTMODULE, ORDERNUMBER,DISPLAYNAME, ENABLED, CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,application) Values (nextval('SEQ_EG_ACTION'), 'PropTax Rev Petition Special notice','/revPetition/revPetition-generateSpecialNotice.action', NULL, (SELECT ID FROM EG_MODULE WHERE NAME = 'Ptis Revision Petition'),1,'PropTax Rev Petition Special notice',false,'ptis',0,1,now(),1,now(),(select id from eg_module where name='Property Tax' and parentmodule is null));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'ULB Operator'),(select id FROM eg_action WHERE NAME = 'PropTax Rev Petition Special notice' and CONTEXTROOT='ptis'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Property Administrator'),(select id FROM eg_action WHERE NAME = 'PropTax Rev Petition rejection' and CONTEXTROOT='ptis'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Super User'),(select id FROM eg_action WHERE NAME = 'PropTax Rev Petition rejection' and CONTEXTROOT='ptis'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'ULB Operator'),(select id FROM eg_action WHERE NAME = 'PropTax Rev Petition rejection' and CONTEXTROOT='ptis'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Property Verifier'),(select id FROM eg_action WHERE NAME = 'PropTax Rev Petition rejection' and CONTEXTROOT='ptis'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'CSC Operator'),(select id FROM eg_action WHERE NAME = 'PropTax report viewer' and CONTEXTROOT='ptis'));

