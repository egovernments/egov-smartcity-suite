INSERT into eg_roleaction values((select id from eg_role where name='Council Management Admin'),(select id from eg_action where name='New-Committteetype'));

INSERT into eg_roleaction values((select id from eg_role where name='Council Management Admin'),(select id from eg_action where name='Create-CouncilCommittee'));

INSERT into eg_roleaction values((select id from eg_role where name='Council Management Admin'),(select id from eg_action where name='View-CouncilCommittee'));

INSERT into eg_roleaction values((select id from eg_role where name='Council Management Admin'),(select id from eg_action where name='Result-CouncilCommittee'));

INSERT into eg_roleaction values((select id from eg_role where name='Council Management Admin'),(select id from eg_action where name='Search and View-CouncilCommittee'));

INSERT into eg_roleaction values((select id from eg_role where name='Council Management Admin'),(select id from eg_action where name='Search and Edit-CouncilCommittee'));

INSERT into eg_roleaction values((select id from eg_role where name='Council Management Admin'),(select id from eg_action where name='Update-CouncilCommitteetype'));

INSERT into eg_roleaction values((select id from eg_role where name='Council Management Admin'),(select id from eg_action where name='Edit-CouncilCommitteetype'));