-----------------Role action mappings to search NonTendered Activities for Measurement Book----------------
insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'WorksMBSearchNonTenderedActivities','/measurementbook/ajax-searchreactivities',null,(select id from EG_MODULE where name = 'WorksMeasurementBook'),1,'Search NonTendered Activities for Measurement Book','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='WorksMBSearchNonTenderedActivities' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Creator'),(select id from eg_action where name ='WorksMBSearchNonTenderedActivities' and contextroot = 'egworks'));

-----------------Role action mappings to get search NonTendered activities form for Measurement Book----------------
insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'WorksMBSearchNonTenderedActivitiesForm','/measurementbook/searchreactivityform',null,(select id from EG_MODULE where name = 'WorksMeasurementBook'),1,'Search NonTendered Activities for Measurement Book Form','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='WorksMBSearchNonTenderedActivitiesForm' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Creator'),(select id from eg_action where name ='WorksMBSearchNonTenderedActivitiesForm' and contextroot = 'egworks'));

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Creator') and actionid = (SELECT id FROM eg_action WHERE name ='WorksMBSearchNonTenderedActivitiesForm' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='WorksMBSearchNonTenderedActivitiesForm' and contextroot = 'egworks');
--rollback delete FROM EG_ACTION WHERE name = 'WorksMBSearchNonTenderedActivitiesForm' and contextroot = 'egworks';

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Creator') and actionid = (SELECT id FROM eg_action WHERE name ='WorksMBSearchNonTenderedActivities' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='WorksMBSearchNonTenderedActivities' and contextroot = 'egworks');
--rollback delete FROM EG_ACTION WHERE name = 'WorksMBSearchNonTenderedActivities' and contextroot = 'egworks';

--eg_feature_action for create MB

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksMBSearchNonTenderedActivitiesForm') ,(select id FROM eg_feature WHERE name = 'Create Abstract MB'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksMBSearchNonTenderedActivities') ,(select id FROM eg_feature WHERE name = 'Create Abstract MB'));


--rollback delete FROM eg_feature_action WHERE ACTION = (select id FROM eg_action  WHERE name = 'WorksMBSearchNonTenderedActivitiesForm');
--rollback delete FROM eg_feature_action WHERE ACTION = (select id FROM eg_action  WHERE name = 'WorksMBSearchNonTenderedActivities');
