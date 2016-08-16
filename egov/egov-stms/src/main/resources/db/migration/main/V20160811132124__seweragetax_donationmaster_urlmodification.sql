update eg_action set url = '/masters/donationView' where name = 'ViewDonationMaster' and contextroot = 'stms';
update eg_action set url = '/masters/donationUpdate' where name = 'ModifyDonationMaster' and contextroot = 'stms';
update eg_action set url = '/masters/success' where name = 'DonationMasterSuccess' and contextroot = 'stms';
update eg_action set url = '/masters/rateView' where name = 'viewSewerageRatesMaster' and contextroot = 'stms';

update eg_roleaction set roleid = (select id from eg_role where name = 'Sewerage Tax Administrator') where actionid = (select id from eg_action where name = 'DonationMaster'and contextroot = 'stms') and roleid = (select id from eg_role where name = 'Property Administrator');
update eg_roleaction set roleid = (select id from eg_role where name = 'Sewerage Tax Administrator') where actionid = (select id from eg_action where name = 'DonationMasterSuccess'and contextroot = 'stms') and roleid = (select id from eg_role where name = 'Property Administrator');
update eg_roleaction set roleid = (select id from eg_role where name = 'Sewerage Tax Administrator') where actionid = (select id from eg_action where name = 'AjaxExistingDonationMasterValidate'and contextroot = 'stms') and roleid = (select id from eg_role where name = 'Property Administrator');

