insert into eg_roleaction(roleid,actionid) values ((select id from eg_role where name = 'CSC Operator'), (select id from eg_action where name = 'TransferOwnership-Form'));
insert into eg_roleaction(roleid,actionid) values ((select id from eg_role where name = 'CSC Operator'), (select id from eg_action where name = 'TitleTransfer-redirect'));
insert into eg_roleaction(roleid,actionid) values ((select id from eg_role where name = 'CSC Operator'), (select id from eg_action where name = 'calculateMutationFees'));

insert into eg_roleaction(roleid,actionid) values ((select id from eg_role where name = 'CSC Operator'), (select id from eg_action where name = 'RevisionPetition-Form'));
insert into eg_roleaction(roleid,actionid) values ((select id from eg_role where name = 'CSC Operator'), (select id from eg_action where name = 'GeneralRevisionPetition-Form'));