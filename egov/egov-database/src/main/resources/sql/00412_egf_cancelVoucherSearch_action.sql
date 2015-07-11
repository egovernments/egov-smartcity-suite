Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot) 
values(nextval('SEQ_EG_ACTION'),'cancelVoucherSearch','/voucher/cancelVoucher-search.action',
null,1,'cancelVoucherSearch',false,'EGF');

Insert into eg_roleaction   values((select id from eg_role where name='Super User'),(select id from eg_action where name='cancelVoucherSearch'));



