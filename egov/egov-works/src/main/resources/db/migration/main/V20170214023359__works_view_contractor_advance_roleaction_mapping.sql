---View contractor advance Role action mapping
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Voucher Creator'), (select id from eg_action where name = 'ViewContractorAdvance' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Voucher Approver'), (select id from eg_action where name = 'ViewContractorAdvance' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Payment Approver'), (select id from eg_action where name = 'ViewContractorAdvance' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Payment Creator'), (select id from eg_action where name = 'ViewContractorAdvance' and contextroot = 'egworks'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ViewContractorAdvance'), (select id FROM eg_feature WHERE name = 'Create Payment Advance'));

--rollback delete FROM eg_feature_action WHERE ACTION = (select id FROM eg_action  WHERE name = 'ViewContractorAdvance') and FEATURE = (select id FROM eg_feature WHERE name = 'Create Payment Advance');
--rollback delete from eg_roleaction where actionid in(select id from eg_action where name='ViewContractorAdvance') and roleid in(select id from eg_role where name in('Voucher Creator','Voucher Approver','Payment Approver','Payment Creator'));