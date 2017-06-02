
INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version,createdby, createddate, lastmodifiedby, lastmodifieddate,application) VALUES (nextval('SEQ_EG_ACTION'), 'CreateUsageSlabMaster', '/masters/usageslab-create', null,(select id from eg_module where name='UsageSlabMaster'), 1, 'Create Usage Slab', true,'wtms', 0, 1, now(), 1, now(),(select id from eg_module where name='Water Tax Management'));
INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version,createdby, createddate, lastmodifiedby, lastmodifieddate,application) VALUES (nextval('SEQ_EG_ACTION'), 'ModifyUsageSlabMaster', '/masters/usageslab-edit/', null,(select id from eg_module where name='UsageSlabMaster'), null, 'Modify Usage Slab', false,'wtms', 0, 1, now(), 1, now(),(select id from eg_module where name='Water Tax Management'));
INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version,createdby, createddate, lastmodifiedby, lastmodifieddate,application) VALUES (nextval('SEQ_EG_ACTION'), 'ModifyUsageSlabMasterSearch', '/masters/usageslab-edit', null,(select id from eg_module where name='UsageSlabMaster'), 3, 'Modify Usage Slab', true,'wtms', 0, 1, now(), 1, now(),(select id from eg_module where name='Water Tax Management'));

INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version,createdby, createddate, lastmodifiedby, lastmodifieddate,application) VALUES (nextval('SEQ_EG_ACTION'), 'ViewUsageSlabMaster', '/masters/usageslab-view', null,(select id from eg_module where name='UsageSlabMaster'), 2, 'View Usage Slab', true,'wtms', 0, 1, now(), 1, now(),(select id from eg_module where name='Water Tax Management'));
INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version,createdby, createddate, lastmodifiedby, lastmodifieddate,application) VALUES (nextval('SEQ_EG_ACTION'), 'ViewUsageSlabMasterRecord', '/masters/usageslab-view/', null,(select id from eg_module where name='UsageSlabMaster'), null, 'View Usage Slab Record', false,'wtms', 0, 1, now(), 1, now(),(select id from eg_module where name='Water Tax Management'));

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='SYSTEM'),
(select id from eg_action where name='CreateUsageSlabMaster'));
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='SYSTEM'),
(select id from eg_action where name='ModifyUsageSlabMaster'));
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='SYSTEM'),
(select id from eg_action where name='ModifyUsageSlabMasterSearch'));
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='SYSTEM'),
(select id from eg_action where name='ViewUsageSlabMaster'));
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='SYSTEM'),
(select id from eg_action where name='ViewUsageSlabMasterRecord'));


Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Property Administrator'),
(select id from eg_action where name='CreateUsageSlabMaster'));
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Property Administrator'),
(select id from eg_action where name='ModifyUsageSlabMaster'));
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Property Administrator'),
(select id from eg_action where name='ModifyUsageSlabMasterSearch'));
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Property Administrator'),
(select id from eg_action where name='ViewUsageSlabMaster'));
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Property Administrator'),
(select id from eg_action where name='ViewUsageSlabMasterRecord'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Usage Slab Master','Create Usage Slab Master Screen',(select id from eg_module  where name = 'Water Tax Management')); 

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Modify Usage Slab Master','Modify Usage Slab Master Screen',(select id from eg_module  where name = 'Water Tax Management')); 

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Usage Slab Master','View Usage Slab Master Screen',(select id from eg_module  where name = 'Water Tax Management')); 


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'CreateUsageSlabMaster') ,(select id FROM eg_feature WHERE name = 'Create Usage Slab Master'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ModifyUsageSlabMaster') ,(select id FROM eg_feature WHERE name = 'Modify Usage Slab Master'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ModifyUsageSlabMasterSearch') ,(select id FROM eg_feature WHERE name = 'Modify Usage Slab Master'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ViewUsageSlabMaster') ,(select id FROM eg_feature WHERE name = 'View Usage Slab Master'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ViewUsageSlabMasterRecord') ,(select id FROM eg_feature WHERE name = 'View Usage Slab Master'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Property Administrator') ,(select id FROM eg_feature WHERE name = 'Create Usage Slab Master'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'SYSTEM') ,(select id FROM eg_feature WHERE name = 'Create Usage Slab Master'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Property Administrator') ,(select id FROM eg_feature WHERE name = 'Modify Usage Slab Master'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'SYSTEM') ,(select id FROM eg_feature WHERE name = 'Modify Usage Slab Master'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Property Administrator') ,(select id FROM eg_feature WHERE name = 'View Usage Slab Master'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'SYSTEM') ,(select id FROM eg_feature WHERE name = 'View Usage Slab Master'));


INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version,createdby, createddate, lastmodifiedby, lastmodifieddate,application) VALUES (nextval('SEQ_EG_ACTION'), 'AjaxUsageSlabOverlapCheck', '/masters/usageslab-overlap-ajax', null,(select id from eg_module where name='UsageSlabMaster'), 6, 'Ajax Usage Slab Overlap Check', false,'wtms', 0, 1, now(), 1, now(),(select id from eg_module where name='Water Tax Management'));
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='SYSTEM'),
(select id from eg_action where name='AjaxUsageSlabOverlapCheck'));
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Property Administrator'),
(select id from eg_action where name='AjaxUsageSlabOverlapCheck'));

INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version,createdby, createddate, lastmodifiedby, lastmodifieddate,application) VALUES (nextval('SEQ_EG_ACTION'), 'AjaxUsageSlabGapCheck', '/masters/usageslab-gap-ajax', null,(select id from eg_module where name='UsageSlabMaster'), 7, 'Ajax Usage Slab Gap Check', false,'wtms', 0, 1, now(), 1, now(),(select id from eg_module where name='Water Tax Management'));
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='SYSTEM'),
(select id from eg_action where name='AjaxUsageSlabGapCheck'));
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Property Administrator'),
(select id from eg_action where name='AjaxUsageSlabGapCheck'));

INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version,createdby, createddate, lastmodifiedby, lastmodifieddate,application) VALUES (nextval('SEQ_EG_ACTION'), 'AjaxUsageSlabRateExistsCheck', '/masters/usageslab-rate-exists-ajax', null,(select id from eg_module where name='UsageSlabMaster'), 8, 'Ajax Usage Slab Rate Exists Check', false,'wtms', 0, 1, now(), 1, now(),(select id from eg_module where name='Water Tax Management'));
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='SYSTEM'),
(select id from eg_action where name='AjaxUsageSlabRateExistsCheck'));
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Property Administrator'),
(select id from eg_action where name='AjaxUsageSlabRateExistsCheck'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxUsageSlabOverlapCheck') ,(select id FROM eg_feature WHERE name = 'Create Usage Slab Master'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxUsageSlabGapCheck') ,(select id FROM eg_feature WHERE name = 'Create Usage Slab Master'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxUsageSlabRateExistsCheck') ,(select id FROM eg_feature WHERE name = 'Modify Usage Slab Master'));


--rollback delete from eg_feature_role where feature = (select id FROM eg_feature WHERE name = 'View Usage Slab Master') and role = (select id from eg_role where name = 'SYSTEM');
--rollback delete from eg_feature_role where feature = (select id FROM eg_feature WHERE name = 'View Usage Slab Master') and role = (select id from eg_role where name = 'Property Administrator');
--rollback delete from eg_feature_action where feature = (select id FROM eg_feature WHERE name = 'View Usage Slab Master') and action = (select id FROM eg_action  WHERE name = 'ViewUsageSlabMaster');
--rollback delete from eg_feature_action where feature = (select id FROM eg_feature WHERE name = 'View Usage Slab Master') and action = (select id FROM eg_action  WHERE name = 'ViewUsageSlabMasterRecord');
--rollback delete from eg_feature where name='View Usage Slab Master' and module=(select id from eg_module  where name = 'Water Tax Management');

--rollback delete from eg_feature_role where feature = (select id FROM eg_feature WHERE name = 'Modify Usage Slab Master') and role = (select id from eg_role where name = 'SYSTEM');
--rollback delete from eg_feature_role where feature = (select id FROM eg_feature WHERE name = 'Modify Usage Slab Master') and role = (select id from eg_role where name = 'Property Administrator');
--rollback delete from eg_feature_action where feature = (select id FROM eg_feature WHERE name = 'Modify Usage Slab Master') and action = (select id FROM eg_action  WHERE name = 'ModifyUsageSlabMaster');
--rollback delete from eg_feature_action where feature = (select id FROM eg_feature WHERE name = 'Modify Usage Slab Master') and action = (select id FROM eg_action  WHERE name = 'AjaxUsageSlabRateExistsCheck');
--rollback delete from eg_feature_action where feature = (select id FROM eg_feature WHERE name = 'Modify Usage Slab Master') and action = (select id FROM eg_action  WHERE name = 'ModifyUsageSlabMasterSearch');
--rollback delete from eg_feature where name='Modify Usage Slab Master' and module=(select id from eg_module  where name = 'Water Tax Management');

--rollback delete from eg_feature_role where feature = (select id FROM eg_feature WHERE name = 'Create Usage Slab Master') and role = (select id from eg_role where name = 'SYSTEM');
--rollback delete from eg_feature_role where feature = (select id FROM eg_feature WHERE name = 'Create Usage Slab Master') and role = (select id from eg_role where name = 'Property Administrator');
--rollback delete from eg_feature_action where feature = (select id FROM eg_feature WHERE name = 'Create Usage Slab Master') and action = (select id FROM eg_action  WHERE name = 'CreateUsageSlabMaster');
--rollback delete from eg_feature_action where feature = (select id FROM eg_feature WHERE name = 'Create Usage Slab Master') and action = (select id FROM eg_action  WHERE name = 'AjaxUsageSlabOverlapCheck');
--rollback delete from eg_feature_action where feature = (select id FROM eg_feature WHERE name = 'Create Usage Slab Master') and action = (select id FROM eg_action  WHERE name = 'AjaxUsageSlabGapCheck');
--rollback delete from eg_feature where name='Create Usage Slab Master' and module=(select id from eg_module  where name = 'Water Tax Management');

--rollback delete from eg_roleaction where actionid = (select id from eg_action where name='CreateUsageSlabMaster') and roleid = (select id from eg_role where name = 'SYSTEM');
--rollback delete from eg_roleaction where actionid = (select id from eg_action where name='ModifyUsageSlabMaster') and roleid = (select id from eg_role where name = 'SYSTEM');
--rollback delete from eg_roleaction where actionid = (select id from eg_action where name='ModifyUsageSlabMasterSearch') and roleid = (select id from eg_role where name = 'SYSTEM');
--rollback delete from eg_roleaction where actionid = (select id from eg_action where name='ViewUsageSlabMaster') and roleid = (select id from eg_role where name = 'SYSTEM');
--rollback delete from eg_roleaction where actionid = (select id from eg_action where name='ViewUsageSlabMasterRecord') and roleid = (select id from eg_role where name = 'SYSTEM');
--rollback delete from eg_roleaction where actionid = (select id from eg_action where name='CreateUsageSlabMaster') and roleid = (select id from eg_role where name = 'Property Administrator');
--rollback delete from eg_roleaction where actionid = (select id from eg_action where name='ModifyUsageSlabMaster') and roleid = (select id from eg_role where name = 'Property Administrator');
--rollback delete from eg_roleaction where actionid = (select id from eg_action where name='ModifyUsageSlabMasterSearch') and roleid = (select id from eg_role where name = 'Property Administrator');
--rollback delete from eg_roleaction where actionid = (select id from eg_action where name='ViewUsageSlabMaster') and roleid = (select id from eg_role where name = 'Property Administrator');
--rollback delete from eg_roleaction where actionid = (select id from eg_action where name='ViewUsageSlabMasterRecord') and roleid = (select id from eg_role where name = 'Property Administrator');
--rollback delete from eg_action where name = 'CreateUsageSlabMaster' and parentmodule=(select id from eg_module where name='UsageSlabMaster');
--rollback delete from eg_action where name = 'ModifyUsageSlabMaster' and parentmodule=(select id from eg_module where name='UsageSlabMaster');
--rollback delete from eg_action where name = 'ModifyUsageSlabMasterSearch' and parentmodule=(select id from eg_module where name='UsageSlabMaster');
--rollback delete from eg_action where name = 'ViewUsageSlabMaster' and parentmodule=(select id from eg_module where name='UsageSlabMaster');
--rollback delete from eg_action where name = 'ViewUsageSlabMasterRecord' and parentmodule=(select id from eg_module where name='UsageSlabMaster');

--rollback delete from eg_roleaction where actionid = (select id from eg_action where name='AjaxUsageSlabOverlapCheck') and roleid = (select id from eg_role where name = 'SYSTEM');
--rollback delete from eg_roleaction where actionid = (select id from eg_action where name='AjaxUsageSlabOverlapCheck') and roleid = (select id from eg_role where name = 'Property Administrator');
--rollback delete from eg_roleaction where actionid = (select id from eg_action where name='AjaxUsageSlabGapCheck') and roleid = (select id from eg_role where name = 'SYSTEM');
--rollback delete from eg_roleaction where actionid = (select id from eg_action where name='AjaxUsageSlabGapCheck') and roleid = (select id from eg_role where name = 'Property Administrator');
--rollback delete from eg_roleaction where actionid = (select id from eg_action where name='AjaxUsageSlabRateExistsCheck') and roleid = (select id from eg_role where name = 'SYSTEM');
--rollback delete from eg_roleaction where actionid = (select id from eg_action where name='AjaxUsageSlabRateExistsCheck') and roleid = (select id from eg_role where name = 'Property Administrator');
--rollback delete from eg_action where name = 'AjaxUsageSlabOverlapCheck' and parentmodule=(select id from eg_module where name='UsageSlabMaster');
--rollback delete from eg_action where name = 'AjaxUsageSlabGapCheck' and parentmodule=(select id from eg_module where name='UsageSlabMaster');
--rollback delete from eg_action where name = 'AjaxUsageSlabRateExistsCheck' and parentmodule=(select id from eg_module where name='UsageSlabMaster');

