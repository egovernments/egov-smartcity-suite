Insert into eg_roleaction values((select id from eg_role where name='Voucher Creator'),(select id from eg_action where name='cancelVoucherSearch'));

Insert into eg_roleaction values((select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='Voucher View'));

Insert into eg_roleaction values((select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='jv-beforemodify'));

Insert into eg_roleaction values((select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='Direct Bank Payments-View'));

Insert into eg_roleaction values((select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='payment-voucherprint'));

Insert into eg_roleaction values((select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='payment-voucherAjaxprint'));

Insert into eg_roleaction values((select id from eg_role where name='Financial Administrator'),(select id from eg_action where name='Chart Of Accounts'));

Insert into eg_roleaction values((select id from eg_role where name='Financial Administrator'),(select id from eg_action where name='common-tree-coa'));
Insert into eg_roleaction values((select id from eg_role where name='Financial Administrator'),(select id from eg_action where name='exil-ChartOfaccountsView'));
Insert into eg_roleaction values((select id from eg_role where name='Financial Administrator'),(select id from eg_action where name='Get Next GLcode'));
