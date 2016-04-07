update eg_action set displayname='Bank Advice for RTGS Payment' where name ='BankAdviceReportforRTGSBankPayment';
update eg_action set displayname='Dishonored Cheque Report' where name ='DishonoredCheque-Search';
update eg_action set enabled=false where name in ('RtgsIssueRegisterReport-exportHtml','RtgsIssueRegisterReport-exportPdf','RtgsIssueRegisterReport-exportXls','AjaxLoadRTGSNumberByAccountId');
update eg_action set enabled=false where name ='BankAdviceReport-search';