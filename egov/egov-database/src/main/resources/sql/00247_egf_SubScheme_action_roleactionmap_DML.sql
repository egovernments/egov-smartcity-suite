update eg_action set url = '/masters/subScheme-newForm.action' where name = 'Create Sub Scheme';

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot) 
values(nextval('SEQ_EG_ACTION'),'createSubScheme','/masters/subScheme-create.action',
(select id from eg_module where name='Schemes'),1,'Search Scheme',false,'EGF');

Insert into eg_roleaction   values((select id from eg_role where name='Super User'),(select id from eg_action where name='createSubScheme'));



Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot) 
values(nextval('SEQ_EG_ACTION'),'searchSubScheme','/masters/subScheme-search.action',
(select id from eg_module where name='Schemes'),1,'Search SubScheme',false,'EGF');

Insert into eg_roleaction   values((select id from eg_role where name='Super User'),(select id from eg_action where name='searchSubScheme'));



--rollback delete from EG_ROLEACTION_MAP where ROLEID in(select id from eg_role where name='Super User') and actionid in (select id FROM eg_action  WHERE name='createSubScheme') ;
--rollback delete from eg_action where name='createSubScheme';

--rollback delete from EG_ROLEACTION_MAP where ROLEID in(select id from eg_role where name='Super User') and actionid in (select id FROM eg_action  WHERE name='searchSubScheme') ;
--rollback delete from eg_action where name='searchSubScheme';


