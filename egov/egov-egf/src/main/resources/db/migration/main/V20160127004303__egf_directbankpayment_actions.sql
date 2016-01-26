Insert into eg_roleaction values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='AjaxMiscReceiptScheme'));

Insert into eg_roleaction values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='AjaxMiscReceiptFundSource'));

Insert into eg_roleaction values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='AjaxMiscReceiptSubScheme'));

Insert into eg_roleaction values((select id from eg_role where name='Payment Approver'),(select id from eg_action where name='AjaxMiscReceiptScheme'));

Insert into eg_roleaction values((select id from eg_role where name='Payment Approver'),(select id from eg_action where name='AjaxMiscReceiptFundSource'));

Insert into eg_roleaction values((select id from eg_role where name='Payment Approver'),(select id from eg_action where name='AjaxMiscReceiptSubScheme'));

Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,
lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'DirectBankPaymentsendForApproval','/payment/directBankPayment-sendForApproval.action',null,(select id from eg_module where name='EGF-COMMON'),1,'DirectBankPaymentsendForApproval',false,'EGF',0,1,current_date,1,current_date,(select id from eg_module where name = 'EGF' and parentmodule is null));


Insert into eg_roleaction   values((select id from eg_role where name='Super User'),(select id from eg_action where name='DirectBankPaymentsendForApproval'));
Insert into eg_roleaction   values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='DirectBankPaymentsendForApproval'));
Insert into eg_roleaction   values((select id from eg_role where name='Payment Approver'),(select id from eg_action where name='DirectBankPaymentsendForApproval'));

