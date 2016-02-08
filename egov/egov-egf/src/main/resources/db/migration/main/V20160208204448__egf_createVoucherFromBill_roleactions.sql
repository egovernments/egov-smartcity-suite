Insert into eg_roleaction   values((select id from eg_role where name='Voucher Creator'),(select id from eg_action where name='Generate PJV'));

Insert into eg_roleaction   values((select id from eg_role where name='Voucher Creator'),(select id from eg_action where name='BillVouchersList'));

Insert into eg_roleaction   values((select id from eg_role where name='Voucher Creator'),(select id from eg_action where name='view Voucher'));

Insert into eg_roleaction   values((select id from eg_role where name='Voucher Creator'),(select id from eg_action where name='PreApprovedVoucherSave'));
