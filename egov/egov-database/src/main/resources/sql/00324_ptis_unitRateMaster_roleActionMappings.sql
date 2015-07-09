INSERT INTO EG_ACTION(ID, NAME, URL, QUERYPARAMS, PARENTMODULE, ORDERNUMBER,DISPLAYNAME, ENABLED, CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE)
Values (nextval('SEQ_EG_ACTION'), 'Unit Rate Master','/admin/unitRate-newForm.action', NULL, (SELECT ID FROM EG_MODULE WHERE NAME = 'PTIS-Administration'),2, 'Unit Rate',true,'ptis',0,1,now(),1,now());

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where name LIKE 'Property Administrator'),(select id FROM eg_action WHERE NAME = 'Unit Rate Master' and CONTEXTROOT='ptis'));


INSERT INTO EG_ACTION(ID, NAME, URL, QUERYPARAMS, PARENTMODULE, ORDERNUMBER,DISPLAYNAME, ENABLED, CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE)
Values (nextval('SEQ_EG_ACTION'), 'Unit Rate Create','/admin/unitRate-create.action', NULL, (SELECT ID FROM EG_MODULE WHERE NAME = 'PTIS-Administration'),3, 'Unit Rate Create',false,'ptis',0,1,now(),1,now());

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where name LIKE 'Property Administrator'),(select id FROM eg_action WHERE NAME = 'Unit Rate Create' and CONTEXTROOT='ptis'));



--rollback delete from EG_ROLEACTION where actionid=(select id from eg_action where name='Unit Rate Create');
--rollback delete from EG_ACTION where name='Unit Rate Create';
--rollback delete from EG_ROLEACTION where actionid=(select id from eg_action where name='Unit Rate Master');
--rollback delete from EG_ACTION where name='Unit Rate Master';

