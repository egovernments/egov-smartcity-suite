update eg_wf_types set link = '/EGF/payment/basePayment-viewInboxItems.action?paymentid=:ID' where type = 'Paymentheader';



Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,
lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'Payment sendForApproval','/payment/payment-sendForApproval.action',null,(select id from eg_module where name='Payments'),1,'Payment Search',false,'EGF',0,1,current_date,1,current_date,(select id from eg_module where name = 'EGF' and parentmodule is null));


Insert into eg_roleaction   values((select id from eg_role where name='Super User'),(select id from eg_action where name='Payment sendForApproval'));
Insert into eg_roleaction   values((select id from eg_role where name='Voucher Creator'),(select id from eg_action where name='Payment sendForApproval'));

Insert into eg_roleaction   values((select id from eg_role where name='Voucher Approver'),(select id from eg_action where name='Payment sendForApproval'));

