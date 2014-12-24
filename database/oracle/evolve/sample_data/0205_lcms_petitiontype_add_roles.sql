#UP


INSERT INTO EG_ROLEACTION_MAP ( ROLEID, ACTIONID ) VALUES (
(select ID_ROLE from eg_roles where ROLE_NAME like 'LCO'), (select ID from EG_ACTION where NAME like 'Create Petitiontype'));

INSERT INTO EG_ROLEACTION_MAP ( ROLEID, ACTIONID ) VALUES (
(select ID_ROLE from eg_roles where ROLE_NAME like 'LAW OFFICER'), (select ID from EG_ACTION where NAME like 'Create Petitiontype'));


#DOWN

delete from EG_ROLEACTION_MAP where ROLEID in (select ID_ROLE from eg_roles where ROLE_NAME like 'LCO') and ACTIONID in(select ID from EG_ACTION where NAME like 'Create Petitiontype');
delete from EG_ROLEACTION_MAP where ROLEID in (select ID_ROLE from eg_roles where ROLE_NAME like 'LAW OFFICER') and ACTIONID in(select ID from EG_ACTION where NAME like 'Create Petitiontype');
