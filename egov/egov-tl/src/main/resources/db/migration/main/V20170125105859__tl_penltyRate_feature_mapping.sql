INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Penalty Rate',
'View Penalty Rate',
(select id from eg_module  where name = 'Trade License'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'penaltyratesview') ,(select id FROM eg_feature WHERE name = 'View Penalty Rate'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'penaltyratessearchview') ,(select id FROM eg_feature WHERE name = 'View Penalty Rate'));

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='Super User') ,(select id FROM eg_feature WHERE name = 'View Penalty Rate'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='TLCreator') ,(select id FROM eg_feature WHERE name = 'View Penalty Rate'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='TLAdmin') ,(select id FROM eg_feature WHERE name = 'View Penalty Rate'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='TLApprover') ,(select id FROM eg_feature WHERE name = 'View Penalty Rate'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='Collection Operator') ,(select id FROM eg_feature WHERE name = 'View Penalty Rate'));
