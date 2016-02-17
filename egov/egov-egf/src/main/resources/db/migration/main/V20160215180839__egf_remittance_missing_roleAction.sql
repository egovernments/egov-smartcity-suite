 Insert into eg_roleaction values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='recovery-crud'));

update eg_action set displayname  = 'Deduction Detailed Report' where displayname  = 'Pending TDS';
update eg_action set displayname  = 'Deductions Remittance Summary' where displayname  = 'TDS Summary';
