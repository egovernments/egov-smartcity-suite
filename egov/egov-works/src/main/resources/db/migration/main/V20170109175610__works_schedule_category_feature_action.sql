insert into eg_feature_action (ACTION, FEATURE) values ((select id FROM eg_action  WHERE name = 'ScheduleCategorySuccess') ,(select id from eg_feature where name = 'Create Schedule Category'));
insert into eg_feature_action (ACTION, FEATURE) values ((select id FROM eg_action  WHERE name = 'ScheduleCategorySuccess') ,(select id from eg_feature where name = 'Modify Schedule Category'));

--rollback delete from EG_FEATUTE_ACTION where action = (select id from eg_action  WHERE name = 'ScheduleCategorySuccess')