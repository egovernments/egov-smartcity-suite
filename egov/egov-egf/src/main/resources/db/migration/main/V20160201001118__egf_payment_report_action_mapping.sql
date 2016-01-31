Insert into eg_roleaction values((select id from eg_role where name='Payment Approver'),(select id from eg_action where name='payment-viewInboxItems'));

Insert into eg_roleaction values((select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='GeneralLedgerReport-searchDrilldown'));

Insert into eg_roleaction values((select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='payment-print'));

Insert into eg_roleaction values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='Voucher View'));
Insert into eg_roleaction values((select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='payment-view'));

Insert into eg_roleaction values((select id from eg_role where name='Payment Approver'),(select id from eg_action where name='Payment sendForApproval'));
Insert into eg_roleaction values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='Payment sendForApproval'));