insert into eg_roleaction values((select id from eg_role where name = 'Property Administrator'), (select id from eg_action where name = 'CloseWaterConnectionAcknowldgement'));
insert into eg_roleaction values((select id from eg_role where name = 'Property Administrator'), (select id from eg_action where name = 'ReConnectionAcknowldgement'));
