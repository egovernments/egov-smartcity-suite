#UP

insert into eg_appconfig_values(id,key_id, effective_from,value)
values(seq_eg_appconfig_values.nextval,(select id from  eg_appconfig where key_name='PurchaseBillApprovalStatus'),to_date('01/04/2009','dd/MM/yyyy'),'SBILL|Approved');

insert into eg_appconfig_values(id,key_id, effective_from,value)
values(seq_eg_appconfig_values.nextval,(select id from  eg_appconfig where key_name='PurchaseBillApprovalStatus'),to_date('01/04/2009','dd/MM/yyyy'),'PURCHBILL|Pending');

insert into eg_appconfig_values(id,key_id, effective_from,value)
values(seq_eg_appconfig_values.nextval,(select id from  eg_appconfig where key_name='ExpenseBillApprovalStatus'),to_date('01/04/2009','dd/MM/yyyy'),'CBILL|APPROVED');

insert into eg_appconfig_values(id,key_id, effective_from,value)
values(seq_eg_appconfig_values.nextval,(select id from  eg_appconfig where key_name='ExpenseBillApprovalStatus'),to_date('01/04/2009','dd/MM/yyyy'),'EXPENSEBILL|Approved');

insert into eg_appconfig_values(id,key_id, effective_from,value)
values(seq_eg_appconfig_values.nextval,(select id from  eg_appconfig where key_name='WorksBillApprovalStatus'),to_date('01/04/2009','dd/MM/yyyy'),'WORKSBILL|Pending');

insert into eg_appconfig_values(id,key_id, effective_from,value)
values(seq_eg_appconfig_values.nextval,(select id from  eg_appconfig where key_name='WorksBillApprovalStatus'),to_date('01/04/2009','dd/MM/yyyy'),'CONTRACTORBILL|APPROVED');

insert into eg_appconfig_values(id,key_id, effective_from,value)
values(seq_eg_appconfig_values.nextval,(select id from  eg_appconfig where key_name='SalaryBillApprovalStatus'),to_date('01/04/2009','dd/MM/yyyy'),'SALBILL|Approved');
#DOWN