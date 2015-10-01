update  eg_installment_master  set end_date='2016-03-31' where installment_num='2015' and id_module 
in(select id from eg_module where name='Water Tax Management');