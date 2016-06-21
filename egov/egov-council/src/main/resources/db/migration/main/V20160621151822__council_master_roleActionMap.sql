
update eg_module set enabled=false where name='Council Management' and contextroot='council';

update eg_module set contextroot='council' where name= 'Council Management Master';


INSERT INTO eg_module(id, name, enabled, contextroot, parentmodule, displayname, ordernumber)
    VALUES (nextval('SEQ_EG_MODULE'), 'Council Member', true, 'council', (select id from eg_module where name='Council Management Master'), 'Council Member', 1);

INSERT INTO eg_module(id, name, enabled, contextroot, parentmodule, displayname, ordernumber)
    VALUES (nextval('SEQ_EG_MODULE'), 'Council Party', true, 'council', (select id from eg_module where name='Council Management Master'), 'Council Party', 2);

INSERT INTO eg_module(id, name, enabled, contextroot, parentmodule, displayname, ordernumber)
    VALUES (nextval('SEQ_EG_MODULE'), 'Council Qualification', true, 'council', (select id from eg_module where name='Council Management Master'), 'Qualification', 3);

INSERT INTO eg_module(id, name, enabled, contextroot, parentmodule, displayname, ordernumber)
    VALUES (nextval('SEQ_EG_MODULE'), 'Council Designation', true, 'council', (select id from eg_module where name='Council Management Master'), 'Designation', 4);

INSERT INTO eg_module(id, name, enabled, contextroot, parentmodule, displayname, ordernumber)
    VALUES (nextval('SEQ_EG_MODULE'), 'Council Caste', true, 'council', (select id from eg_module where name='Council Management Master'), 'Caste', 5);

update eg_action set parentmodule=(select id from eg_module where name='Council Caste' and contextroot='council') where name in ('New-CouncilCaste','Create-CouncilCaste','Update-CouncilCaste','View-CouncilCaste','Edit-CouncilCaste','Result-CouncilCaste','Search and View-CouncilCaste','Search and Edit-CouncilCaste','Search and View Result-CouncilCaste','Search and Edit Result-CouncilCaste');

update eg_action set parentmodule=(select id from eg_module where name='Council Qualification' and contextroot='council') where name in ('Search and Edit Result-CouncilQualification','Search and View Result-CouncilQualification','Search and Edit-CouncilQualification','Search and View-CouncilQualification','Result-CouncilQualification','Edit-CouncilQualification','View-CouncilQualification','Update-CouncilQualification','Create-CouncilQualification','New-CouncilQualification');

update eg_action set parentmodule=(select id from eg_module where name='Council Party' and contextroot='council') where name in ('Search and Edit Result-CouncilParty','Search and View Result-CouncilParty','Search and Edit-CouncilParty','Search and View-CouncilParty','Result-CouncilParty','Edit-CouncilParty','View-CouncilParty','Update-CouncilParty','Create-CouncilParty','New-CouncilParty');

update eg_action set parentmodule=(select id from eg_module where name='Council Member' and contextroot='council') where name in ('Search and Edit Result-CouncilMember','Search and View Result-CouncilMember','Search and Edit-CouncilMember','Search and View-CouncilMember','Result-CouncilMember','Edit-CouncilMember','View-CouncilMember','Update-CouncilMember','Create-CouncilMember','New-CouncilMember');

update eg_action set parentmodule=(select id from eg_module where name='Council Designation' and contextroot='council') where name in ('Search and Edit Result-CouncilDesignation','Search and View Result-CouncilDesignation','Search and Edit-CouncilDesignation','Search and View-CouncilDesignation','Result-CouncilDesignation','Edit-CouncilDesignation','View-CouncilDesignation','Update-CouncilDesignation','Create-CouncilDesignation','New-CouncilDesignation');

