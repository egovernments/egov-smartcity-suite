Insert into eg_roleaction values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='Contractor/Supplier Payments'));
Insert into eg_roleaction values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='ajax-common-detailcode'));
Insert into eg_roleaction values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='ajax-common-detailtype'));
Insert into eg_roleaction values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='searchEntries-accountdetail'));
Insert into eg_roleaction values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='ajax-common-entityby20'));
Insert into eg_roleaction values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='DirectBankPayment'));
Insert into eg_roleaction values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='Direct Bank Payments-Create'));
Insert into eg_roleaction values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='ShowFinancialWorkflowHistory'));
Insert into eg_roleaction values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='Payment Search'));
Insert into eg_roleaction values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='Payment Save'));
Insert into eg_roleaction values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='Payment Create'));

Insert into eg_roleaction values((select id from eg_role where name='Payment Approver'),(select id from eg_action where name='ShowFinancialWorkflowHistory'));
