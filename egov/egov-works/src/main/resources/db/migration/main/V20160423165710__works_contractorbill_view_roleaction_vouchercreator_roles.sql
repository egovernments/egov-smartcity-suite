insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Voucher Creator'),(select id from eg_action where name ='WorksViewContractorBill' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Voucher Approver'),(select id from eg_action where name ='WorksViewContractorBill' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Payment Creator'),(select id from eg_action where name ='WorksViewContractorBill' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Payment Approver'),(select id from eg_action where name ='WorksViewContractorBill' and contextroot = 'egworks'));

insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Voucher Creator'),(select id from eg_action where name ='ContractorBillPDF' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Voucher Approver'),(select id from eg_action where name ='ContractorBillPDF' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Payment Creator'),(select id from eg_action where name ='ContractorBillPDF' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Payment Approver'),(select id from eg_action where name ='ContractorBillPDF' and contextroot = 'egworks'));

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Voucher Creator') and actionid = (SELECT id FROM eg_action WHERE name ='ContractorBillPDF' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Voucher Approver') and actionid = (SELECT id FROM eg_action WHERE name ='ContractorBillPDF' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Payment Creator') and actionid = (SELECT id FROM eg_action WHERE name ='ContractorBillPDF' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Payment Approver') and actionid = (SELECT id FROM eg_action WHERE name ='ContractorBillPDF' and contextroot = 'egworks');

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Voucher Creator') and actionid = (SELECT id FROM eg_action WHERE name ='WorksViewContractorBill' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Voucher Approver') and actionid = (SELECT id FROM eg_action WHERE name ='WorksViewContractorBill' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Payment Creator') and actionid = (SELECT id FROM eg_action WHERE name ='WorksViewContractorBill' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Payment Approver') and actionid = (SELECT id FROM eg_action WHERE name ='WorksViewContractorBill' and contextroot = 'egworks');
