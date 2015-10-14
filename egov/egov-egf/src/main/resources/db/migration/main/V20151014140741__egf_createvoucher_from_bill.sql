update eg_action set url = '/voucher/billVoucher-newForm.action' where name ='Generate PJV';

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) 
values(nextval('SEQ_EG_ACTION'),'BillVouchersList','/voucher/billVoucher-lists.action',
(select id from eg_module where name='Bills Accounting'),1,'BillVouchersList',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));

Insert into eg_roleaction   values((select id from eg_role where name='Super User'),(select id from eg_action where name='BillVouchersList'));


Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) 
values(nextval('SEQ_EG_ACTION'),'PreApprovedVoucherSave','/voucher/preApprovedVoucher-save.action',
(select id from eg_module where name='Bills Accounting'),1,'BillVouchersList',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));

Insert into eg_roleaction   values((select id from eg_role where name='Super User'),(select id from eg_action where name='PreApprovedVoucherSave'));



