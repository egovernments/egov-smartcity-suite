
Insert into eg_roleaction   values((select id from eg_role where name='Voucher Creator'),(select id from eg_action where name='Remittance recovery view'));

Insert into eg_roleaction   values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='Remittance recovery view'));

Insert into eg_roleaction   values((select id from eg_role where name='Voucher Approver'),(select id from eg_action where name='Remittance recovery view'));

Insert into eg_roleaction   values((select id from eg_role where name='Payment Approver'),(select id from eg_action where name='Remittance recovery view'));

Insert into eg_roleaction   values((select id from eg_role where name='Super User'),(select id from eg_action where name='Remittance recovery view'));
