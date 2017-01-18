INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Coll_View Access') ,(select id FROM eg_feature WHERE name = 'Common Search Application'));
