Insert into eg_roleaction values((select id from eg_role where name='Voucher Creator'),(select id from eg_action where name='ModifyJVoucher'));
