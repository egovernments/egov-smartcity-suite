
INSERT INTO eg_roleaction (roleid, actionid)  select (select id from eg_role where name='Bank Reconciler'), id from eg_action where name in('BankEntriesNotInBankBookNewForm','BankEntriesNotInBankBookSave','BankEntriesNotInBankBookList','AjaxMiscReceiptScheme',
'AjaxMiscReceiptSubScheme','AjaxMiscReceiptFundSource','ajaxLoadAllBanksByFund','ajaxLoadBankBranchFromBank','ajax-common-detailcode');
