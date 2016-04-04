Insert into eg_roleaction values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='ChequeAssignmentSave'));

