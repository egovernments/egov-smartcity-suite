update eg_installment_master set installment_num='201704' where id_module=(select id from eg_module where name='Trade License') and  installment_year='2017-04-01';
