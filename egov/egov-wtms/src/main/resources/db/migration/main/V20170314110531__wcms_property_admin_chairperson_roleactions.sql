delete from eg_roleaction where actionid = (select id from eg_action where name='EditChairPersonDetailsScreen') and roleid = (select id from eg_role where name = 'Property Administrator');
INSERT INTO eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Property Administrator'),(select id from eg_action where name='EditChairPersonDetailsScreen'));

delete from eg_roleaction where actionid = (select id from eg_action where name='EditChairPersonDetailsScreenValues') and roleid = (select id from eg_role where name = 'Property Administrator');
INSERT INTO eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Property Administrator'),(select id from eg_action where name='EditChairPersonDetailsScreenValues'));

delete from eg_roleaction where actionid = (select id from eg_action where name='ViewChairPersonDetailsScreen') and roleid = (select id from eg_role where name = 'Property Administrator');
INSERT INTO eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Property Administrator'),(select id from eg_action where name='ViewChairPersonDetailsScreen'));

delete from eg_roleaction where actionid = (select id from eg_action where name='activechairpersonexistsasongivendate-ajax') and roleid = (select id from eg_role where name = 'Property Administrator');
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Property Administrator') ,(select id FROM eg_action  WHERE name = 'activechairpersonexistsasongivendate-ajax'));

delete from eg_feature_role where feature = (select id FROM eg_feature WHERE name = 'View ChairPerson Master') and role = (select id from eg_role where name = 'Property Administrator');
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Property Administrator') ,(select id FROM eg_feature WHERE name = 'View ChairPerson Master'));

delete from eg_feature_role where feature = (select id FROM eg_feature WHERE name = 'Edit ChairPerson Master') and role = (select id from eg_role where name = 'Property Administrator');
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Property Administrator') ,(select id FROM eg_feature WHERE name = 'Edit ChairPerson Master'));



