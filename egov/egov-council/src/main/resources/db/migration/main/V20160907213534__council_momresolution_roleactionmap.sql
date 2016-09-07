INSERT into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),
'generateresolutionForMom','/councilmeeting/generateresolution',(select id from eg_module where name='Council Meeting' and 
parentmodule=(select id from eg_module where name='Council Management Transaction')),5,'generateresolutionForMom',false,'council',
(select id from eg_module where name='Council Management' and parentmodule is null));

INSERT into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='generateresolutionForMom'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES ((select id FROM eg_action  WHERE name = 'generateresolutionForMom') ,(select id  FROM eg_feature WHERE name = 'View Meeting' and module in (select id from eg_module  where name = 'Council Management')));

