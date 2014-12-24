#UP

insert into EG_APPCONFIG_VALUES (ID, KEY_ID, EFFECTIVE_FROM, VALUE) values (SEQ_EG_APPCONFIG_VALUES.nextval,(select id from EG_APPCONFIG where KEY_NAME = 'contingencyBillDefaultPurposeId'),sysdate,'28');

insert into eg_appconfig_values (id, key_id, effective_from, value)
values 
(seq_eg_appconfig_values.nextVal,(select id from eg_appconfig where key_name='Communication Expenses' and module='EGF'),sysdate, '1. Whether the monthly rental charges claimed is correct.');
insert into eg_appconfig_values (id, key_id, effective_from, value)
values 
(seq_eg_appconfig_values.nextVal,(select id from eg_appconfig where key_name='Communication Expenses' and module='EGF'),sysdate, '2. Whether the call charges claimed is correct.');
insert into eg_appconfig_values (id, key_id, effective_from, value)
values 
(seq_eg_appconfig_values.nextVal,(select id from eg_appconfig where key_name='Communication Expenses' and module='EGF'),sysdate, '3. Whether the OB has been compared with CB of the previous bill.') ;
insert into eg_appconfig_values (id, key_id, effective_from, value)
values 
(seq_eg_appconfig_values.nextVal,(select id from eg_appconfig where key_name='Communication Expenses' and module='EGF'),sysdate, '4. Whether the call charges does not exceed the permitted calls.'); 

insert into eg_appconfig_values (id, key_id, effective_from, value)
values 
(seq_eg_appconfig_values.nextVal,(select id from eg_appconfig where key_name='Communication Expenses' and module='EGF'),sysdate, '5. Whether the sanction of the competent authority has been obtained if the permitted calls exceeds.');
insert into eg_appconfig_values (id, key_id, effective_from, value)
values 
(seq_eg_appconfig_values.nextVal,(select id from eg_appconfig where key_name='Communication Expenses' and module='EGF'),sysdate, '6. Whether the charges for the excess calls have been remitted in the Treasury and the challan produced in respect of residential phone.');
insert into eg_appconfig_values (id, key_id, effective_from, value)
values 
(seq_eg_appconfig_values.nextVal,(select id from eg_appconfig where key_name='Communication Expenses' and module='EGF'),sysdate, '7. Whether necessary entry has been made in the Standard Register.');

insert into eg_appconfig_values (id, key_id, effective_from, value)
values 
(seq_eg_appconfig_values.nextVal,(select id from eg_appconfig where key_name='Communication Expenses' and module='EGF'),sysdate, '8. Whether the budget provision is available.');
insert into eg_appconfig_values (id, key_id, effective_from, value)
values 
(seq_eg_appconfig_values.nextVal,(select id from eg_appconfig where key_name='Communication Expenses' and module='EGF'),sysdate, '9. Whether the bill has been approved by the competent authority.');
insert into eg_appconfig_values (id, key_id, effective_from, value)
values 
(seq_eg_appconfig_values.nextVal,(select id from eg_appconfig where key_name='Communication Expenses' and module='EGF'),sysdate, '10. Whether the original bill is enclosed.');



insert into eg_appconfig_values (id, key_id, effective_from, value)
values 
(seq_eg_appconfig_values.nextVal,(select id from eg_appconfig where key_name='Electricity Charges' and module='EGF'),sysdate, '1. Whether the claim is in order.');
insert into eg_appconfig_values (id, key_id, effective_from, value)
values 
(seq_eg_appconfig_values.nextVal,(select id from eg_appconfig where key_name='Electricity Charges' and module='EGF'),sysdate, '2. Whether the OB has been compared with CB of the previous bill. ');
insert into eg_appconfig_values (id, key_id, effective_from, value)
values 
(seq_eg_appconfig_values.nextVal,(select id from eg_appconfig where key_name='Electricity Charges' and module='EGF'),sysdate, '3. Whether necessary entry has been made in the Standard Register.') ;
insert into eg_appconfig_values (id, key_id, effective_from, value)
values 
(seq_eg_appconfig_values.nextVal,(select id from eg_appconfig where key_name='Electricity Charges' and module='EGF'),sysdate, '4. Whether the budget provision is available.'); 

insert into eg_appconfig_values (id, key_id, effective_from, value)
values 
(seq_eg_appconfig_values.nextVal,(select id from eg_appconfig where key_name='Electricity Charges' and module='EGF'),sysdate, '5. Whether the bill has been approved by the competent authority.');
insert into eg_appconfig_values (id, key_id, effective_from, value)
values 
(seq_eg_appconfig_values.nextVal,(select id from eg_appconfig where key_name='Electricity Charges' and module='EGF'),sysdate, '6. Whether an extract of the electricity meter reader is enclosed.');

insert into eg_appconfig_values (id, key_id, effective_from, value)
values 
(seq_eg_appconfig_values.nextVal,(select id from eg_appconfig where key_name='Others' and module='EGF'),sysdate, '1. Whether the claim is in order.');

insert into eg_appconfig_values (id, key_id, effective_from, value)
values 
(seq_eg_appconfig_values.nextVal,(select id from eg_appconfig where key_name='Others' and module='EGF'),sysdate, '2. Whether the budget provision is available.');
insert into eg_appconfig_values (id, key_id, effective_from, value)
values 
(seq_eg_appconfig_values.nextVal,(select id from eg_appconfig where key_name='Others' and module='EGF'),sysdate, '3. Whether the bill has been approved by the competent authority. ');
insert into eg_appconfig_values (id, key_id, effective_from, value)
values 
(seq_eg_appconfig_values.nextVal,(select id from eg_appconfig where key_name='Others' and module='EGF'),sysdate, '4. Whether original bill has been enclosed');
#DOWN


