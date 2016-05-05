-----------------Update Role action mappings to search Modify LOA form----------------
update eg_action set DISPLAYNAME = 'Modify LOA' where NAME = 'WorksSearchLetterOfAcceptanceModifyForm' and CONTEXTROOT = 'egworks';

-------------------Role action mappings to search LOA to Modify----------------
insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'AjaxSearchLetterOfAcceptanceToModify','/letterofacceptance/ajaxsearch-loatomodify',null,(select id from EG_MODULE where name = 'WorksLetterOfAcceptance'),0,'AjaxSearchLetterOfAcceptanceToModify','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='AjaxSearchLetterOfAcceptanceToModify' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works Creator'), (select id from eg_action where name = 'AjaxSearchLetterOfAcceptanceToModify' and contextroot = 'egworks'));

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Creator') and actionid = (SELECT id FROM eg_action WHERE name ='AjaxSearchLetterOfAcceptanceToModify' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='AjaxSearchLetterOfAcceptanceToModify' and contextroot = 'egworks');
--rollback delete FROM EG_ACTION WHERE name = 'AjaxSearchLetterOfAcceptanceToModify' and contextroot = 'egworks';