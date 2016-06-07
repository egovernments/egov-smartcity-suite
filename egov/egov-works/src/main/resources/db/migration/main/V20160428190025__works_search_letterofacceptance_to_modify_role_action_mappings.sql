-----------------Role action mappings to search Modify LOA form----------------
insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'WorksSearchLetterOfAcceptanceModifyForm','/searchletterofacceptance/searchmodifyform',null,(select id from EG_MODULE where name = 'WorksLetterOfAcceptance'),3,'Search/Modify LOA','true','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='WorksSearchLetterOfAcceptanceModifyForm' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works Creator'), (select id from eg_action where name = 'WorksSearchLetterOfAcceptanceModifyForm' and contextroot = 'egworks'));

-------------------Role action mappings to search LOA to Modify----------------
--insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'AjaxSearchLetterOfAcceptanceToModify','/letterofacceptance/ajaxsearch-loatomodify',null,(select id from EG_MODULE where name = 'WorksLetterOfAcceptance'),0,'AjaxSearchLetterOfAcceptanceToModify','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
--insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='AjaxSearchLetterOfAcceptanceToModify' and contextroot = 'egworks'));
--insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works Creator'), (select id from eg_action where name = 'AjaxSearchLetterOfAcceptanceToModify' and contextroot = 'egworks'));

-----------------Role action mappings to Modify LOA----------------------
insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'WorksModifyLetterOfAcceptance','/letterofacceptance/modify',null,(select id from EG_MODULE where name = 'WorksLetterOfAcceptance'),1,'Modify LOA','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='WorksModifyLetterOfAcceptance' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Creator'),(select id from eg_action where name ='WorksModifyLetterOfAcceptance' and contextroot = 'egworks'));

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Creator') and actionid = (SELECT id FROM eg_action WHERE name ='WorksModifyLetterOfAcceptance' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='WorksModifyLetterOfAcceptance' and contextroot = 'egworks');
--rollback delete FROM EG_ACTION WHERE name = 'WorksModifyLetterOfAcceptance' and contextroot = 'egworks';

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Creator') and actionid = (SELECT id FROM eg_action WHERE name ='WorksSearchLetterOfAcceptanceModifyForm' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='WorksSearchLetterOfAcceptanceModifyForm' and contextroot = 'egworks');
--rollback delete FROM EG_ACTION WHERE name = 'WorksSearchLetterOfAcceptanceModifyForm' and contextroot = 'egworks';

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Creator') and actionid = (SELECT id FROM eg_action WHERE name ='AjaxSearchLetterOfAcceptanceToModify' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='AjaxSearchLetterOfAcceptanceToModify' and contextroot = 'egworks');
--rollback delete FROM EG_ACTION WHERE name = 'AjaxSearchLetterOfAcceptanceToModify' and contextroot = 'egworks';