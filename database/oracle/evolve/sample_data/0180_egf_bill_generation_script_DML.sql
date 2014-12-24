#UP
INSERT into eg_script( id, name, script_type, script, start_date, end_date, created_by, created_date, modified_by, modified_date)
values
(eg_script_seq.nextVal,'egf.bill.number.generator','python','result="EJV"+"/"+sequenceGenerator.getNextNumber("EJV",1).getFormattedNumber().zfill(4)+"/"+year',sysdate,'01-mar-2099',1, sysdate,1,sysdate);

insert into eg_appconfig_values (id, key_id, effective_from, value)
values 
(seq_eg_appconfig_values.nextVal,(select id from eg_appconfig where key_name='Bill_Number_Geneartion_Auto' and module='EGF'),sysdate, 'Y');
#DOWN