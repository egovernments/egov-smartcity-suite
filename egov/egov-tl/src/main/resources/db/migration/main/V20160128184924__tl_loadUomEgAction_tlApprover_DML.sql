Insert into eg_roleaction values((select id from eg_role where name='TLApprover'),(select id from eg_action where name='Ajax-loadUomName'));
