--view MB
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Voucher Creator'),(select id from eg_action where name ='ViewMeasurementBook' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Voucher Approver'),(select id from eg_action where name ='ViewMeasurementBook' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Payment Creator'),(select id from eg_action where name ='ViewMeasurementBook' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Payment Approver'),(select id from eg_action where name ='ViewMeasurementBook' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Bill Creator'),(select id from eg_action where name ='ViewMeasurementBook' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Bill Approver'),(select id from eg_action where name ='ViewMeasurementBook' and contextroot = 'egworks'));

--view MB PDF
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Voucher Creator'),(select id from eg_action where name ='MeasurementBookPDF' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Voucher Approver'),(select id from eg_action where name ='MeasurementBookPDF' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Payment Creator'),(select id from eg_action where name ='MeasurementBookPDF' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Payment Approver'),(select id from eg_action where name ='MeasurementBookPDF' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Bill Creator'),(select id from eg_action where name ='MeasurementBookPDF' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Bill Approver'),(select id from eg_action where name ='MeasurementBookPDF' and contextroot = 'egworks'));

--view Abstract Estimate
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Voucher Creator'),(select id from eg_action where name ='AbstractEstimateView' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Voucher Approver'),(select id from eg_action where name ='AbstractEstimateView' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Payment Creator'),(select id from eg_action where name ='AbstractEstimateView' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Payment Approver'),(select id from eg_action where name ='AbstractEstimateView' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Bill Creator'),(select id from eg_action where name ='AbstractEstimateView' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Bill Approver'),(select id from eg_action where name ='AbstractEstimateView' and contextroot = 'egworks'));

--view Abstract Estimate PDF
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Voucher Creator'),(select id from eg_action where name ='AbstractEstimatePDF' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Voucher Approver'),(select id from eg_action where name ='AbstractEstimatePDF' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Payment Creator'),(select id from eg_action where name ='AbstractEstimatePDF' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Payment Approver'),(select id from eg_action where name ='AbstractEstimatePDF' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Bill Creator'),(select id from eg_action where name ='AbstractEstimatePDF' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Bill Approver'),(select id from eg_action where name ='AbstractEstimatePDF' and contextroot = 'egworks'));

--view Abstract Estimate BOQ XLS
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Voucher Creator'),(select id from eg_action where name ='ViewBillOfQuatities' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Voucher Approver'),(select id from eg_action where name ='ViewBillOfQuatities' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Payment Creator'),(select id from eg_action where name ='ViewBillOfQuatities' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Payment Approver'),(select id from eg_action where name ='ViewBillOfQuatities' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Bill Creator'),(select id from eg_action where name ='ViewBillOfQuatities' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Bill Approver'),(select id from eg_action where name ='ViewBillOfQuatities' and contextroot = 'egworks'));

--view Line estimate
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Voucher Creator'),(select id from eg_action where name ='WorksViewLineEstimate' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Voucher Approver'),(select id from eg_action where name ='WorksViewLineEstimate' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Payment Creator'),(select id from eg_action where name ='WorksViewLineEstimate' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Payment Approver'),(select id from eg_action where name ='WorksViewLineEstimate' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Bill Creator'),(select id from eg_action where name ='WorksViewLineEstimate' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Bill Approver'),(select id from eg_action where name ='WorksViewLineEstimate' and contextroot = 'egworks'));

--view Line estimate PDF
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Voucher Creator'),(select id from eg_action where name ='LineEstimatePDF' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Voucher Approver'),(select id from eg_action where name ='LineEstimatePDF' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Payment Creator'),(select id from eg_action where name ='LineEstimatePDF' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Payment Approver'),(select id from eg_action where name ='LineEstimatePDF' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Bill Creator'),(select id from eg_action where name ='LineEstimatePDF' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Bill Approver'),(select id from eg_action where name ='LineEstimatePDF' and contextroot = 'egworks'));

--view LOA
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Voucher Creator'),(select id from eg_action where name ='WorksViewLetterOfAcceptance' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Voucher Approver'),(select id from eg_action where name ='WorksViewLetterOfAcceptance' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Payment Creator'),(select id from eg_action where name ='WorksViewLetterOfAcceptance' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Payment Approver'),(select id from eg_action where name ='WorksViewLetterOfAcceptance' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Bill Creator'),(select id from eg_action where name ='WorksViewLetterOfAcceptance' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Bill Approver'),(select id from eg_action where name ='WorksViewLetterOfAcceptance' and contextroot = 'egworks'));

--view LOA PDF
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Voucher Creator'),(select id from eg_action where name ='LetterOfAcceptancePDF' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Voucher Approver'),(select id from eg_action where name ='LetterOfAcceptancePDF' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Payment Creator'),(select id from eg_action where name ='LetterOfAcceptancePDF' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Payment Approver'),(select id from eg_action where name ='LetterOfAcceptancePDF' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Bill Creator'),(select id from eg_action where name ='LetterOfAcceptancePDF' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Bill Approver'),(select id from eg_action where name ='LetterOfAcceptancePDF' and contextroot = 'egworks'));

--view Asset
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Voucher Creator'),(select id from eg_action where name ='View-Asset' and contextroot = 'egassets'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Voucher Approver'),(select id from eg_action where name ='View-Asset' and contextroot = 'egassets'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Payment Creator'),(select id from eg_action where name ='View-Asset' and contextroot = 'egassets'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Payment Approver'),(select id from eg_action where name ='View-Asset' and contextroot = 'egassets'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Bill Creator'),(select id from eg_action where name ='View-Asset' and contextroot = 'egassets'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Bill Approver'),(select id from eg_action where name ='View-Asset' and contextroot = 'egassets'));

--rollback delete from eg_roleaction where actionid in(select id from eg_action where name='ViewMeasurementBook' and contextroot='egworks') and roleid in(select id from eg_role where name in ('Voucher Creator','Voucher Approver','Payment Creator','Payment Approver','Bill Creator','Bill Approver'));
--rollback delete from eg_roleaction where actionid in(select id from eg_action where name='MeasurementBookPDF' and contextroot='egworks') and roleid in(select id from eg_role where name in ('Voucher Creator','Voucher Approver','Payment Creator','Payment Approver','Bill Creator','Bill Approver'));

--rollback delete from eg_roleaction where actionid in(select id from eg_action where name='AbstractEstimateView' and contextroot='egworks') and roleid in(select id from eg_role where name in ('Voucher Creator','Voucher Approver','Payment Creator','Payment Approver','Bill Creator','Bill Approver'));
--rollback delete from eg_roleaction where actionid in(select id from eg_action where name='AbstractEstimatePDF' and contextroot='egworks') and roleid in(select id from eg_role where name in ('Voucher Creator','Voucher Approver','Payment Creator','Payment Approver','Bill Creator','Bill Approver'));
--rollback delete from eg_roleaction where actionid in(select id from eg_action where name='ViewBillOfQuatities' and contextroot='egworks') and roleid in(select id from eg_role where name in ('Voucher Creator','Voucher Approver','Payment Creator','Payment Approver','Bill Creator','Bill Approver'));

--rollback delete from eg_roleaction where actionid in(select id from eg_action where name='WorksViewLineEstimate' and contextroot='egworks') and roleid in(select id from eg_role where name in ('Voucher Creator','Voucher Approver','Payment Creator','Payment Approver','Bill Creator','Bill Approver'));
--rollback delete from eg_roleaction where actionid in(select id from eg_action where name='LineEstimatePDF' and contextroot='egworks') and roleid in(select id from eg_role where name in ('Voucher Creator','Voucher Approver','Payment Creator','Payment Approver','Bill Creator','Bill Approver'));

--rollback delete from eg_roleaction where actionid in(select id from eg_action where name='WorksViewLetterOfAcceptance' and contextroot='egworks') and roleid in(select id from eg_role where name in ('Voucher Creator','Voucher Approver','Payment Creator','Payment Approver','Bill Creator','Bill Approver'));
--rollback delete from eg_roleaction where actionid in(select id from eg_action where name='LetterOfAcceptancePDF' and contextroot='egworks') and roleid in(select id from eg_role where name in ('Voucher Creator','Voucher Approver','Payment Creator','Payment Approver','Bill Creator','Bill Approver'));

--rollback delete from eg_roleaction where actionid in(select id from eg_action where name='View-Asset' and contextroot='egassets') and roleid in(select id from eg_role where name in ('Voucher Creator','Voucher Approver','Payment Creator','Payment Approver','Bill Creator','Bill Approver'));
