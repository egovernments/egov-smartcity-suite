
Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot) 
values(nextval('SEQ_EG_ACTION'),'searchScheme','/masters/scheme-search.action',
(select id from eg_module where name='Schemes'),1,'Search Scheme',false,'EGF');

Insert into eg_roleaction   values((select id from eg_role where name='Super User'),(select id from eg_action where name='searchScheme'));


Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot) 
values(nextval('SEQ_EG_ACTION'),'editScheme','/masters/scheme-edit.action',
(select id from eg_module where name='Schemes'),1,'Search Scheme',false,'EGF');

Insert into eg_roleaction   values((select id from eg_role where name='Super User'),(select id from eg_action where name='editScheme'));



--rollback delete from eg_roleaction where ROLEID in(select id from eg_role where name='Super User') and actionid in (select id FROM eg_action  WHERE name='searchScheme') ;
--rollback delete from eg_action where name='editScheme';

--rollback delete from eg_roleaction where ROLEID in(select id from eg_role where name='Super User') and actionid in (select id FROM eg_action  WHERE name='searchScheme') ;
--rollback delete from eg_action where name='editScheme';


