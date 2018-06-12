
delete from eg_roleaction where actionid = (select id from eg_action where name='digitalSignature-WaterTaxPreviewWorkflow' and contextroot='wtms') and roleid = (select id from eg_role where name='Water Tax Approver');

insert into eg_roleaction (actionid, roleid) values ((select id from eg_action where name='digitalSignature-WaterTaxPreviewWorkflow' and contextroot='wtms'),(select id from eg_role where name='Water Tax Approver'));

delete from eg_roleaction where actionid = (select id from eg_action where name='digitalSignature-SignWorkOrder' and contextroot='wtms') and roleid = (select id from eg_role where name='Water Tax Approver');

insert into eg_roleaction (actionid, roleid) values ((select id from eg_action where name='digitalSignature-SignWorkOrder' and contextroot='wtms'),(select id from eg_role where name='Water Tax Approver'));

delete from eg_roleaction where actionid = (select id from eg_action where name='Water Tax Search Pending Digital Signature' and contextroot='wtms') and roleid = (select id from eg_role where name='Water Tax Approver');

insert into eg_roleaction (actionid, roleid) values ((select id from eg_action where name='Water Tax Search Pending Digital Signature' and contextroot='wtms'),(select id from eg_role where name='Water Tax Approver'));

