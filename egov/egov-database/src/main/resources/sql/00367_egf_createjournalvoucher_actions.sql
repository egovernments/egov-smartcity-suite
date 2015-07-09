update eg_action set url = '/voucher/journalVoucher-newForm.action' where name = 'Journal Voucher-Create';

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot) 
values(nextval('SEQ_EG_ACTION'),'ajaLoadAllCoaCodes','/voucher/common-ajaLoadAllCoaCodes.action',
null,1,'ajaLoadAllCoaCodes',false,'EGF');

Insert into eg_roleaction  values((select id from eg_role where name='Super User'),(select id from eg_action where name='ajaLoadAllCoaCodes'));


Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot) 
values(nextval('SEQ_EG_ACTION'),'JournalVoucherCreate','/voucher/journalVoucher-create.action',
null,1,'Journal Voucher Create',false,'EGF');

Insert into eg_roleaction  values((select id from eg_role where name='Super User'),(select id from eg_action where name='JournalVoucherCreate'));

 
