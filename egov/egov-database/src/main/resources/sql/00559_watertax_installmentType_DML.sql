update eg_installment_master SET installment_type  ='Yearly' where id_module=(select id from eg_module where name ='Water Tax Management') and description='2015-16';

--rollback update eg_installment_master set installment_type='' where id_module=(select id from eg_module where name ='Water Tax Management') and description='2015-16';
