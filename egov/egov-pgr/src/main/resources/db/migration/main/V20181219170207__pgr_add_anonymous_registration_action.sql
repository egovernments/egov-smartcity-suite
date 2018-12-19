INSERT INTO eg_action(id,name,url,parentmodule,ordernumber,displayname, enabled, contextroot,version,
                     createdby,createddate,lastmodifiedby,lastmodifieddate,application) 
VALUES 
(NEXTVAL('SEQ_EG_ACTION'),'ANONYMOUS_GRIEVANCE_REGISTER','/grievance/register/by-anonymous',
 (SELECT id FROM eg_module WHERE name='PGR_MAIN'),0,'ANONYMOUS_GRIEVANCE_REGISTER',false,
 'pgr',0,(select id from eg_user where username='system'),CURRENT_TIMESTAMP,
 (select id from eg_user where username='system'),CURRENT_TIMESTAMP,(SELECT id FROM eg_module WHERE name='PGR'));
 
INSERT INTO EG_ROLEACTION (roleid,actionid) VALUES ((SELECT id FROM eg_role WHERE name='PUBLIC'), 
(SELECT id FROM eg_action WHERE name ='ANONYMOUS_GRIEVANCE_REGISTER'));

UPDATE eg_feature_action SET action=(SELECT id FROM eg_action where name='GRIEVANCE_TYPE_BY_NAME') 
WHERE feature = (select id from eg_feature where name='Citizen Grievance Registration')
AND action=(SELECT id FROM eg_action where name='GRIEVANCE_TYPE_BY_CATEGORY');

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Anonymous Grievance Registration',
                                                           'Anonymous Grievance Registration',
(select id from eg_module  where name = 'PGR'));

INSERT INTO eg_feature_role(feature,role) 
VALUES ((SELECT id FROM eg_feature WHERE name='Anonymous Grievance Registration'), 
       (SELECT id FROM eg_role WHERE name='PUBLIC'));

INSERT INTO eg_feature_action(feature,action) VALUES
((SELECT id FROM eg_feature where name='Anonymous Grievance Registration'),
(SELECT id FROM eg_action WHERE name='ANONYMOUS_GRIEVANCE_REGISTER'));

INSERT INTO eg_feature_action(feature,action) VALUES
((SELECT id FROM eg_feature where name='Anonymous Grievance Registration'),
(SELECT id FROM eg_action WHERE name='GRIEVANCE_TYPE_BY_NAME'));

INSERT INTO eg_feature_action(feature,action) VALUES
((SELECT id FROM eg_feature where name='Anonymous Grievance Registration'),
(SELECT id FROM eg_action WHERE name='GRIEVANCE_REGISTER_LOCATIONS'));