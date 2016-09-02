INSERT INTO eg_module(id, name, enabled, contextroot, parentmodule, displayname, ordernumber)  VALUES 
(nextval('SEQ_EG_MODULE'), 'Council MOM', true, 'council', (select id from eg_module where name='Council Meeting'), 'Council MOM', 4);

update eg_action set parentmodule = (select id from eg_module where name='Council MOM') where name='Create-CouncilMOM';
update eg_action set parentmodule = (select id from eg_module where name='Council MOM') where name='Search and View-CouncilMOM';
update eg_action set parentmodule = (select id from eg_module where name='Council MOM') where name='Search and Edit-CouncilMOM';
