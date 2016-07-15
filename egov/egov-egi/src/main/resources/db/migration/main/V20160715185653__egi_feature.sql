create sequence seq_eg_feature;

create table eg_feature(
 id numeric primary key,
 name varchar(100),
 description varchar(200),
 module bigint,
 version numeric default 0,
 CONSTRAINT unq_feature_name UNIQUE (name)
);

create table eg_feature_action(
 action bigint,
 feature numeric,
 CONSTRAINT fk_feature_action FOREIGN KEY (action) REFERENCES eg_action (id),
 CONSTRAINT fk_feature FOREIGN KEY (feature) REFERENCES eg_feature (id)
);

create table eg_feature_role(
 role bigint,
 feature numeric,
 CONSTRAINT fk_feature_role FOREIGN KEY (role) REFERENCES eg_role (id),
 CONSTRAINT fk_featurerole FOREIGN KEY (feature) REFERENCES eg_feature (id)
);

INSERT INTO eg_module (id, name, enabled, contextroot, parentmodule, displayname, ordernumber) 
VALUES (NEXTVAL('seq_eg_module'), 'EGI-FeatureAccessControl', true, NULL, (select id from eg_module where name='User Module'), 'Feature Access Control', 3);

INSERT INTO EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) VALUES 
(NEXTVAL('SEQ_EG_ACTION'),'AccessControlByFeature','/feature/access-control/by-feature', null,(select id from EG_MODULE where name = 'EGI-FeatureAccessControl'),1,
'Access Control By Feature',true,'egi',0,1,now(),1,now(),(select id from eg_module  where name = 'Administration'));

INSERT INTO EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) VALUES 
(NEXTVAL('SEQ_EG_ACTION'),'AccessControlByRole','/feature/access-control/by-role', null,(select id from EG_MODULE where name = 'EGI-FeatureAccessControl'),1,
'Access Control By Role',true,'egi',0,1,now(),1,now(),(select id from eg_module  where name = 'Administration'));

INSERT INTO EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) VALUES 
(NEXTVAL('SEQ_EG_ACTION'),'GrantAccessControlByFeature','/feature/access-control/grant', null,(select id from EG_MODULE where name = 'EGI-FeatureAccessControl'),1,
'GrantAccessControlByFeature',false,'egi',0,1,now(),1,now(),(select id from eg_module  where name = 'Administration'));

INSERT INTO EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) VALUES 
(NEXTVAL('SEQ_EG_ACTION'),'RevokeAccessControlByRole','/feature/access-control/revoke', null,(select id from EG_MODULE where name = 'EGI-FeatureAccessControl'),1,
'RevokeAccessControlByRole',false,'egi',0,1,now(),1,now(),(select id from eg_module  where name = 'Administration'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_action  WHERE name = 'AccessControlByFeature'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID)
VALUES
((select id from eg_role where name = 'Super User') ,(select id FROM eg_action  WHERE name = 'AccessControlByRole'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID)
VALUES
((select id from eg_role where name = 'Super User') ,(select id FROM eg_action  WHERE name = 'GrantAccessControlByFeature'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID)
VALUES
((select id from eg_role where name = 'Super User') ,(select id FROM eg_action  WHERE name = 'RevokeAccessControlByRole'));






