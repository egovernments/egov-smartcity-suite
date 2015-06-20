INSERT INTO EG_ACTION(ID, NAME, URL, QUERYPARAMS, PARENTMODULE, ORDERNUMBER,DISPLAYNAME, ENABLED, CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE)
Values (nextval('SEQ_EG_ACTION'), 'Rebate Master','/rebatePeriod/create', NULL, (SELECT ID FROM EG_MODULE WHERE NAME = 'PTIS-Administration'),3, 'Rebate Master',true,'ptis',0,1,now(),1,now());

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where name LIKE 'Property Administrator'),(select id FROM eg_action WHERE NAME = 'LoginForm' and CONTEXTROOT='egi'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where name LIKE 'Property Administrator'),(select id FROM eg_action WHERE NAME = 'Rebate Master' and CONTEXTROOT='ptis'));


--rollback delete from EG_ROLEACTION where actionid=(select id from eg_action where name='Rebate Master');
--rollback delete from EG_ROLEACTION where actionid=(select id from eg_action where name='LoginForm');
--rollback delete from EG_ACTION where name='Rebate Master';
