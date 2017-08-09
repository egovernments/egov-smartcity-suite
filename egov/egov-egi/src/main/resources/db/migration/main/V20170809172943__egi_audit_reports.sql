INSERT INTO EG_ACTION (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version,
 createdby, createddate, lastmodifiedby, lastmodifieddate, application) values 
(NEXTVAL('SEQ_EG_ACTION'),'User Role Audit Report','/audit/report/user-role',null,
(select id from eg_module where name like 'Audit Reports')
,0,'User Role Audit Report',true,'egi',0,1,now(),1,now(),(select id from eg_module where name='Administration'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name='SYSTEM'),
(select id from eg_action where name='User Role Audit Report'));


INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE,VERSION) VALUES (NEXTVAL('seq_eg_feature'),'User Role Audit Report','Systemwide User Role Mapping Audit Report',(select id from eg_module  where name = 'Administration'),0);

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'User Role Audit Report') ,(select id FROM eg_feature WHERE name = 'User Role Audit Report'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'User By Username') ,(select id FROM eg_feature WHERE name = 'User Role Audit Report'));

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'SYSTEM') ,(select id FROM eg_feature WHERE name = 'User Role Audit Report'));


INSERT INTO EG_ACTION (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version,
 createdby, createddate, lastmodifiedby, lastmodifieddate, application) values 
(NEXTVAL('SEQ_EG_ACTION'),'User Password Audit Report','/audit/report/user-password',null,
(select id from eg_module where name like 'Audit Reports')
,0,'User Password Audit Report',true,'egi',0,1,now(),1,now(),(select id from eg_module where name='Administration'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name='SYSTEM'),
(select id from eg_action where name='User Password Audit Report'));


INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE,VERSION) VALUES (NEXTVAL('seq_eg_feature'),'User Password Audit Report','Systemwide User Password Change Audit Report',(select id from eg_module  where name = 'Administration'),0);

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'User Password Audit Report') ,(select id FROM eg_feature WHERE name = 'User Password Audit Report'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'User By Username') ,(select id FROM eg_feature WHERE name = 'User Password Audit Report'));

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'SYSTEM') ,(select id FROM eg_feature WHERE name = 'User Password Audit Report'));



INSERT INTO EG_ACTION (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version,
 createdby, createddate, lastmodifiedby, lastmodifieddate, application) values 
(NEXTVAL('SEQ_EG_ACTION'),'Feature Role Audit Report','/audit/report/feature-role',null,
(select id from eg_module where name like 'Audit Reports')
,0,'Feature Role Audit Report',true,'egi',0,1,now(),1,now(),(select id from eg_module where name='Administration'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name='SYSTEM'),
(select id from eg_action where name='Feature Role Audit Report'));


INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE,VERSION) VALUES (NEXTVAL('seq_eg_feature'),'Feature Role Audit Report','Systemwide Feature Role Mapping Audit Report',(select id from eg_module  where name = 'Administration'),0);

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Feature Role Audit Report') ,(select id FROM eg_feature WHERE name = 'Feature Role Audit Report'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'ListFeaturesByModule') ,(select id FROM eg_feature WHERE name = 'Feature Role Audit Report'));

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'SYSTEM') ,(select id FROM eg_feature WHERE name = 'Feature Role Audit Report'));
