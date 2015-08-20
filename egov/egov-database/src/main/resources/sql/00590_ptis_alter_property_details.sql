alter table egpt_property_detail add column corrAddressDiff boolean;

delete from eg_roleaction where actionid = (select id from eg_action where name = 'Search by GIS');

--rollback alter table egpt_property_detail drop column corrAddressDiff;