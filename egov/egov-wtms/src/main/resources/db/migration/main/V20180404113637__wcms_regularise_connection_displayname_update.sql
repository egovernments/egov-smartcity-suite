
update eg_action set displayname='Apply to Regularize Connection' where displayname='Apply for Regularise Connection' and contextroot='wtms';

delete from eg_roleaction where actionid = (select id from eg_action where name='ReassignWaterChargesApplication' and contextroot='wtms') and roleid = (select id from eg_role where name='Water Tax Approver');

insert into eg_roleaction (actionid, roleid) values ((select id from eg_action where name='ReassignWaterChargesApplication' and contextroot='wtms'),(select id from eg_role where name='Water Tax Approver'));
