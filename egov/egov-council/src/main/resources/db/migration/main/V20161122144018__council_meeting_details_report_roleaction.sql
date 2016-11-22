-------Role Action Mapping for council meeting details report--------------------

INSERT into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Meeting Details Report','/councilreports/meetingdetails/search',(select id from eg_module where name='Council Management Reports'),3,'Meeting Details Report',true,'council',(select id from eg_module where name='Council Management' and parentmodule is null));

INSERT into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Meeting Details Report'));

INSERT into eg_roleaction values((select id from eg_role where name='Council Management Admin'),(select id from eg_action where name='Meeting Details Report'));

INSERT into eg_roleaction values((select id from eg_role where name='Council Clerk'),(select id from eg_action where name='Meeting Details Report'));

-------Feature Action Mapping for council meeting details report--------------------

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Meeting Details Report','Meeting Details Report',(select id from eg_module  where name = 'Council Management'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Meeting Details Report') ,(select id FROM eg_feature WHERE name = 'Meeting Details Report'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Meeting Details Report'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Council Management Admin') ,(select id FROM eg_feature WHERE name = 'Meeting Details Report'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Council Clerk') ,(select id FROM eg_feature WHERE name = 'Meeting Details Report'));