update eg_wf_matrix set currentstate = 'NEW',nextstate = 'Accounts Officer Approved' where objecttype = 'CVoucherHeader' and currentstate = 'Create VoucherHeader:NEW';

update eg_wf_matrix set currentstate = 'Rejected',nextstate = 'Accounts Officer Approved' , validactions = 'Forward,Cancel' where objecttype = 'CVoucherHeader' and currentstate = 'Create VoucherHeader:Rejected';

update eg_wf_matrix set currentstate = 'Accounts Officer Approved',nextstate = 'Examiner of Accounts Approved' where objecttype = 'CVoucherHeader' and currentstate = 'Create VoucherHeader:Accounts Officer Approved';

update eg_wf_matrix set currentstate = 'Examiner of Accounts Approved',nextstate = 'END' where objecttype = 'CVoucherHeader' and currentstate = 'Create VoucherHeader:Examiner of Accounts Approved';



update eg_wf_matrix set currentstate = 'NEW',nextstate = 'Accounts Officer Approved' where objecttype = 'Paymentheader' and currentstate = 'Create PaymentHeader:NEW';

update eg_wf_matrix set currentstate = 'Rejected',nextstate = 'Accounts Officer Approved' , validactions = 'Forward,Cancel' where objecttype = 'Paymentheader' and currentstate = 'Create PaymentHeader:Rejected';

update eg_wf_matrix set currentstate = 'Accounts Officer Approved',nextstate = 'Examiner of Accounts Approved' where objecttype = 'Paymentheader' and currentstate = 'Create PaymentHeader:Accounts Officer Approved';

update eg_wf_matrix set currentstate = 'Examiner of Accounts Approved',nextstate = 'END' where objecttype = 'Paymentheader' and currentstate = 'Create PaymentHeader:Examiner of Accounts Approved';




