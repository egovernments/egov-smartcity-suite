Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'BankEntriesNotInBankBookList','/payment/bankEntriesNotInBankBook-search.action',(select id from eg_module where name='Payments' ),1,'Bank Statement Entries - Not In Bank Book',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));

Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='BankEntriesNotInBankBookList'));


Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'AjaxDeleteBankEntries','/payment/ajaxDeleteBankEntries.action',(select id from eg_module where name='Payments' ),1,'Bank Statement Entries - Not In Bank Book',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));

Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='AjaxDeleteBankEntries'));

