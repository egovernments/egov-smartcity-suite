INSERT INTO eg_module (id, name, enabled, contextroot, parentmodule, displayname, ordernumber) VALUES (nextval('seq_eg_module'), 'Role', true, NULL, (select id from eg_module where name='User Module'), 'Role', 1);
INSERT INTO eg_module (id, name, enabled, contextroot, parentmodule, displayname, ordernumber) VALUES (nextval('seq_eg_module'), 'Role Mapping', true, NULL, (select id from eg_module where name='User Module'), 'Role Mapping', 2);

UPDATE eg_action SET parentModule=(SELECT id FROM eg_module WHERE name='Role') where name in ('CreateRoleForm','ViewRoleForm','UpdateRoleForm');
UPDATE eg_action SET parentModule=(SELECT id FROM eg_module WHERE name='Role Mapping') where name = 'ViewuserRoleForm';
UPDATE eg_action SET displayname='Create Role' where name='CreateRoleForm';