Insert into eg_action(id,name,url,module_id,order_number,display_name,is_enabled,context_root) 
values(nextval('SEQ_EG_ACTION'),'createScheme','/masters/scheme-create.action',
(select id from eg_module where name='Schemes'),1,'Create Scheme',false,'EGF');

Insert into eg_roleaction_map  values((select id from eg_role where name='Super User'),(select id from eg_action where name='createScheme'));

--rollback delete from EG_ROLEACTION_MAP where ROLEID in(select id from eg_role where name='Super User') and actionid in (select id FROM eg_action  WHERE name='createScheme') ;
--rollback delete from eg_action where name='createScheme';
