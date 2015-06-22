 INSERT INTO eg_module(id, name, enabled, contextroot, parentmodule, displayname, ordernumber)
    VALUES (nextval('SEQ_EG_MODULE'), 'Ptis Revision Petition', false, null, (select id from eg_module where name='Property Tax'), 'Ptis Revision Petition', 1);
    
INSERT INTO EG_ACTION(ID, NAME, URL, QUERYPARAMS, PARENTMODULE, ORDERNUMBER,DISPLAYNAME, ENABLED, CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE)
Values (nextval('SEQ_EG_ACTION'), 'PropTax objectionNew','/objection/objection-newForm.action', NULL, (SELECT ID FROM EG_MODULE WHERE NAME = 'Ptis Revision Petition'),1, 'PropTax objectionNew',false,'ptis',0,1,now(),1,now());

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where name LIKE 'CSC Operator'),(select id FROM eg_action WHERE NAME = 'PropTax objectionNew' and CONTEXTROOT='ptis'));


INSERT INTO EG_ACTION(ID, NAME, URL, QUERYPARAMS, PARENTMODULE, ORDERNUMBER,DISPLAYNAME, ENABLED, CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE)
Values (nextval('SEQ_EG_ACTION'), 'PropTax objectionView','/objection/objection-view.action', NULL, (SELECT ID FROM EG_MODULE WHERE NAME = 'Ptis Revision Petition'),2, 'PropTax objectionView',false,'ptis',0,1,now(),1,now());

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where name LIKE 'CSC Operator'),(select id FROM eg_action WHERE NAME = 'PropTax objectionView' and CONTEXTROOT='ptis'));

INSERT INTO EG_ACTION(ID, NAME, URL, QUERYPARAMS, PARENTMODULE, ORDERNUMBER,DISPLAYNAME, ENABLED, CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE)
Values (nextval('SEQ_EG_ACTION'), 'PropTax objectionAction','/objection/objection.action', NULL, (SELECT ID FROM EG_MODULE WHERE NAME = 'Ptis Revision Petition'),3, 'PropTax objectionAction',false,'ptis',0,1,now(),1,now());

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where name LIKE 'CSC Operator'),(select id FROM eg_action WHERE NAME = 'PropTax objectionAction' and CONTEXTROOT='ptis'));

--rollback delete from eg_module where name ='Ptis Revision Petition';
--rollback delete from EG_ROLEACTION where ACTIONID in (select id from eg_action where name in ('PropTax objectionNew','PropTax objectionView','PropTax objectionAction'));
--rollback delete from EG_ACTION where name in ('PropTax objectionNew','PropTax objectionView','PropTax objectionAction');
