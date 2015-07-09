DELETE FROM EG_ROLEACTION where ACTIONID in (SELECT ID FROM EG_ACTION WHERE URL='/search/viewProperty-viewForm.action' and CONTEXTROOT='ptis');
DELETE FROM EG_ACTION WHERE URL='/search/viewProperty-viewForm.action' and CONTEXTROOT='ptis';
INSERT INTO EG_ACTION(ID, NAME, URL, QUERYPARAMS, PARENTMODULE, ORDERNUMBER,DISPLAYNAME, ENABLED, CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE)
Values (nextval('SEQ_EG_ACTION'), 'View Property','/view/viewProperty-viewForm.action', NULL, (SELECT ID FROM EG_MODULE WHERE NAME = 'Existing property'),NULL, 'View Property',false,'ptis',0,1,now(),1,now());

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where UPPER(name) = 'ASSESSOR'),(select id FROM eg_action  WHERE NAME = 'View Property' and CONTEXTROOT='ptis'));

INSERT INTO EG_ACTION(ID, NAME, URL, QUERYPARAMS, PARENTMODULE, ORDERNUMBER,DISPLAYNAME, ENABLED, CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE)
Values (nextval('SEQ_EG_ACTION'), 'Load Admin Boundaries','/common/ajaxCommon-blockByLocality.action', NULL, (SELECT ID FROM EG_MODULE WHERE NAME = 'New Property'),NULL, 'View Property',false,'ptis',0,1,now(),1,now());

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where UPPER(name) = 'ASSESSOR'),(select id FROM eg_action  WHERE NAME = 'Load Admin Boundaries' and CONTEXTROOT='ptis'));


--rollback INSERT INTO EG_ACTION(ID, NAME, URL, QUERYPARAMS, PARENTMODULE, ORDERNUMBER,DISPLAYNAME, ENABLED, CONTEXTROOT, VERSION,CREATEDBY,CREATEDDATE, LASTMODIFIEDBY,LASTMODIFIEDDATE) Values (nextval('SEQ_EG_ACTION'), 'View Property','/search/viewProperty-viewForm.action', NULL, (SELECT ID FROM EG_MODULE WHERE NAME = 'Existing property'),NULL, 'View Property',false,'ptis',0,1,now(),1,now());
--rollback INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) = 'ASSESSOR'),(select id FROM eg_action  WHERE NAME = 'View Property' and CONTEXTROOT='ptis'));
--rollback DELETE FROM EG_ROLEACTION WHERE ACTIONID in(SELECT ID FROM EG_ACTION WHERE NAME='View Property' and CONTEXTROOT='ptis');
--rollback DELETE FROM EG_ACTION WHERE NAME='View Property' and CONTEXTROOT='ptis';
--rollback DELETE FROM EG_ROLEACTION WHERE ACTIONID in(SELECT ID FROM EG_ACTION WHERE NAME='Load Admin Boundaries' and CONTEXTROOT='ptis');
--rollback DELETE FROM EG_ACTION WHERE NAME='Load Admin Boundaries' and CONTEXTROOT='ptis';

