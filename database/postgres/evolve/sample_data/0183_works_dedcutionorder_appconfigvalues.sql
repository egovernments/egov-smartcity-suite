#UP
insert into eg_appconfig_values(id,key_id, effective_from,value)
values(seq_eg_appconfig_values.nextval,(select id from  eg_appconfig where key_name='StatutoryDeductionKey'),to_date('01/04/2009','dd/MM/yyyy'),
'TAX|LOAN|INCOME TAX|SALES TAX|MWGWF');

insert into eg_appconfig_values(id,key_id, effective_from,value)
values(seq_eg_appconfig_values.nextval,(select id from  eg_appconfig where key_name='StandardDeductionKey'),to_date('01/04/2009','dd/MM/yyyy'),
'RETENTION MONEY|INTEREST ON ADVANCE|PENALTY|Retention Money-Revenue|Retention Money-Capital|Tender Deposit-Revenue|Tender Deposit-Capital|Security Deposit-Revenue|Security Deposit-Capital|Forms-Publications-Tenders');

#DOWN

