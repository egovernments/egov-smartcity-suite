--Disabled normal search legalcase
update eg_action set enabled=false where contextroot='lcms' and name='searchlegalcase' and enabled=true;

--elastic search legalcase
Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application)
 values(nextval('SEQ_EG_ACTION'),'elasticsearchlegalcase','/elasticsearch/legalcasesearch/',(select id from eg_module 
 where name='LCMS Transactions'),2,'Search Legal Case',true,'lcms',(select id from eg_module where name='LCMS' 
 and parentmodule is null));
 
Insert into eg_roleaction values((select id from eg_role where name='Super User'),
(select id from eg_action where name='elasticsearchlegalcase'));

--feature action 
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'elasticsearchlegalcase') ,
(select id FROM eg_feature WHERE name = 'Search Legal Case'));