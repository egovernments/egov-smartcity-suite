----------create-------------
delete from eg_roleaction where actionid in (select id from eg_action where name ='Open inbox') and roleid in (select id from eg_role where name='CITIZEN');

insert into eg_roleaction (actionid,roleid) values ((select id from eg_action where name ='View Property'),(select id from eg_role where name='CITIZEN'));


-------------Add/ALt----------------
insert into eg_roleaction (actionid,roleid) values ((select id from eg_action where name ='AlterAssessment-Form'),(select id from eg_role where name='CITIZEN'));

insert into eg_roleaction (actionid,roleid) values ((select id from eg_action where name ='Assessment-commonSearch'),(select id from eg_role where name='CITIZEN'));

insert into eg_roleaction (actionid,roleid) values ((select id from eg_action where name ='Modify Property Form'),(select id from eg_role where name='CITIZEN'));

insert into eg_roleaction (actionid,roleid) values ((select id from eg_action where name ='Forward Modify Property'),(select id from eg_role where name='CITIZEN'));

insert into eg_roleaction (actionid,roleid) values ((select id from eg_action where name ='modifyPropertyAckPrint'),(select id from eg_role where name='CITIZEN'));
------------------------------------------------------GRP------------------------------------------

insert into eg_roleaction (actionid,roleid) values ((select id from eg_action where name ='GeneralRevisionPetition-Form'),(select id from eg_role where name='CITIZEN'));
insert into eg_roleaction (actionid,roleid) values ((select id from eg_action where name ='Assessment-commonSearch'),(select id from eg_role where name='CITIZEN'));
insert into eg_roleaction (actionid,roleid) values ((select id from eg_action where name ='Property Tax Rev Petition Action'),(select id from eg_role where name='CITIZEN'));
insert into eg_roleaction (actionid,roleid) values ((select id from eg_action where name ='RP GRP Ack'),(select id from eg_role where name='CITIZEN'));

-------------------RP-------------------
insert into eg_roleaction (actionid,roleid) values ((select id from eg_action where name ='RevisionPetition-Form'),(select id from eg_role where name='CITIZEN'));
insert into eg_roleaction (actionid,roleid) values ((select id from eg_action where name ='Property Tax Gen Rev Petition New'),(select id from eg_role where name='CITIZEN'));
insert into eg_roleaction (actionid,roleid) values ((select id from eg_action where name ='Property Tax Rev Petition New'),(select id from eg_role where name='CITIZEN'));

--------------------------TT------------------------------
insert into eg_roleaction (actionid,roleid) values ((select id from eg_action where name ='TransferOwnership-Form'),(select id from eg_role where name='CITIZEN'));
insert into eg_roleaction (actionid,roleid) values ((select id from eg_action where name ='TitleTransfer-redirect'),(select id from eg_role where name='CITIZEN'));
insert into eg_roleaction (actionid,roleid) values ((select id from eg_action where name ='Transfer Property Form'),(select id from eg_role where name='CITIZEN'));
insert into eg_roleaction (actionid,roleid) values ((select id from eg_action where name ='Transfer Property Save'),(select id from eg_role where name='CITIZEN'));
insert into eg_roleaction (actionid,roleid) values ((select id from eg_action where name ='Acknowledgement Transfer Property'),(select id from eg_role where name='CITIZEN'));

-------------------------TaxExemption------------------

insert into eg_roleaction (actionid,roleid) values ((select id from eg_action where name ='TaxExemption-Form'),(select id from eg_role where name='CITIZEN'));
insert into eg_roleaction (actionid,roleid) values ((select id from eg_action where name ='Property Exemption Form'),(select id from eg_role where name='CITIZEN'));
insert into eg_roleaction (actionid,roleid) values ((select id from eg_action where name ='Exemption Ack'),(select id from eg_role where name='CITIZEN'));

----------------------------VR------------------
insert into eg_roleaction (actionid,roleid) values ((select id from eg_action where name ='VacancyRemission-Form'),(select id from eg_role where name='CITIZEN'));
insert into eg_roleaction (actionid,roleid) values ((select id from eg_action where name ='vacancyRemissionCreate'),(select id from eg_role where name='CITIZEN'));
insert into eg_roleaction (actionid,roleid) values ((select id from eg_action where name ='Vacancy Remission Ack'),(select id from eg_role where name='CITIZEN'));
