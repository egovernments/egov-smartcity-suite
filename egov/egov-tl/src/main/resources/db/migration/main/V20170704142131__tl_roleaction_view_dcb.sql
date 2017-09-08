insert into eg_roleaction values ((select id from eg_role where name='TLCreator'),(select id from eg_action where url like '/dcb/view'));
insert into eg_roleaction values ((select id from eg_role where name='TLApprover'),(select id from eg_action where url like '/dcb/view'));
insert into eg_roleaction values ((select id from eg_role where name='CSC Operator'),(select id from eg_action where url like '/dcb/view'));
