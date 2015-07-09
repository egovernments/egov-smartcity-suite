INSERT INTO eg_wf_types 
(id,module,type,link,createdby,createddate,lastmodifiedby,lastmodifieddate,renderyn,groupyn,typefqn,displayname) VALUES 
(nextval('seq_eg_wf_types'),(SELECT id FROM eg_module WHERE name='Property Tax'),'PropertyImpl',':ID',1,now(),1,now(), 'Y', 'N', 
'org.egov.ptis.domain.entity.property.PropertyImpl', 'Property' );

INSERT INTO EG_ACTION(ID, NAME, URL, QUERYPARAMS, PARENTMODULE, ORDERNUMBER,DISPLAYNAME, ENABLED, CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE)
Values (nextval('SEQ_EG_ACTION'), 'Open inbox','/create/createProperty-view.action', NULL, (SELECT ID FROM EG_MODULE WHERE NAME = 'New Property'),NULL, 'Open inbox',false,'ptis',0,1,now(),1,now());

INSERT INTO EG_ACTION(ID, NAME, URL, QUERYPARAMS, PARENTMODULE, ORDERNUMBER,DISPLAYNAME, ENABLED, CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE)
Values (nextval('SEQ_EG_ACTION'), 'Approve Property','/create/createProperty-approve.action', NULL, (SELECT ID FROM EG_MODULE WHERE NAME = 'New Property'),NULL, 'Approve Property',false,'ptis',0,1,now(),1,now());

INSERT INTO EG_ACTION(ID, NAME, URL, QUERYPARAMS, PARENTMODULE, ORDERNUMBER,DISPLAYNAME, ENABLED, CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE)
Values (nextval('SEQ_EG_ACTION'), 'Forward Property','/create/createProperty-forward.action', NULL, (SELECT ID FROM EG_MODULE WHERE NAME = 'New Property'),NULL, 'Forward Property',false,'ptis',0,1,now(),1,now());

INSERT INTO EG_ACTION(ID, NAME, URL, QUERYPARAMS, PARENTMODULE, ORDERNUMBER,DISPLAYNAME, ENABLED, CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE)
Values (nextval('SEQ_EG_ACTION'), 'Reject Property','/create/createProperty-reject.action', NULL, (SELECT ID FROM EG_MODULE WHERE NAME = 'New Property'),NULL, 'Reject Property',false,'ptis',0,1,now(),1,now());

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where name = 'CSC Operator'),(select id FROM eg_action  WHERE NAME = 'Open inbox' and CONTEXTROOT='ptis'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where name = 'Property Verifier'),(select id FROM eg_action  WHERE NAME = 'Open inbox' and CONTEXTROOT='ptis'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where name = 'Property Approver'),(select id FROM eg_action  WHERE NAME = 'Open inbox' and CONTEXTROOT='ptis'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where name = 'Property Verifier'),(select id FROM eg_action  WHERE NAME = 'Approve Property' and CONTEXTROOT='ptis'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where name = 'Property Approver'),(select id FROM eg_action  WHERE NAME = 'Approve Property' and CONTEXTROOT='ptis'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where name = 'Property Verifier'),(select id FROM eg_action  WHERE NAME = 'Forward Property' and CONTEXTROOT='ptis'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where name = 'Property Approver'),(select id FROM eg_action  WHERE NAME = 'Forward Property' and CONTEXTROOT='ptis'));

--rollback DELETE FROM EG_ROLEACTION WHERE actionid in(SELECT id FROM eg_action WHERE name in('Open inbox','Approve Property','Forward Property','Reject Property')) and roleid in(SELECT id FROM eg_role WHERE name in ('CSC Operator','Property Verifier','Property Approver'));

--rollback DELETE FROM eg_action WHERE name in ('Open inbox','Approve Property','Forward Property','Reject Property');

--rollback DELETE FROM eg_wf_types WHERE type='PropertyImpl';
