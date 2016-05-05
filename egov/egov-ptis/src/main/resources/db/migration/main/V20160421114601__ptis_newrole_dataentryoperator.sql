--New Role
INSERT INTO eg_role (id, name, description, createddate, createdby, lastmodifiedby, lastmodifieddate, version) 
VALUES (nextval('seq_eg_role'), 'Data Entry Operator', 'Data Entry Operator', now(), 1, 1, now(), 0);

--Roleactions for data entry screens
delete from eg_roleaction  where actionid in(select id from eg_action where name='Edit Demand Form' and contextroot='ptis') and roleid not in (select id from eg_role where name in ('Super User'));
delete from eg_roleaction  where actionid in(select id from eg_action where name='Edit Demand Update' and contextroot='ptis') and roleid not in (select id from eg_role where name in ('Super User'));
delete from eg_roleaction  where actionid in(select id from eg_action where name='Edit Demand submit' and contextroot='ptis') and roleid not in (select id from eg_role where name in ('Super User'));
delete from eg_roleaction  where actionid in(select id from eg_action where name='Edit Demand Update Form' and contextroot='ptis') and roleid not in (select id from eg_role where name in ('Super User'));
insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Property Usage by type'), id from eg_role where name in ('Data Entry Operator');
insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'PTIS-Data Entry Screen'), id from eg_role where name in ('Data Entry Operator');
insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'PTIS-Create Data Entry Screen'), id from eg_role where name in ('Data Entry Operator');
insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Edit Demand Form'), id from eg_role where name in ('Data Entry Operator');
insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Edit Demand Update'), id from eg_role where name in ('Data Entry Operator');
insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Edit Demand submit'), id from eg_role where name in ('Data Entry Operator');
insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Edit Demand Update Form'), id from eg_role where name in ('Data Entry Operator');
insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'AadhaarInfo'), id from eg_role where name in ('Data Entry Operator');
insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'User By Mobile number'), id from eg_role where name in ('Data Entry Operator');
insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Add/Edit Demand form'), id from eg_role where name in ('Data Entry Operator');
insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Load Block By Locality'), id from eg_role where name in ('Data Entry Operator');
insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Populate Categories by Property Type'), id from eg_role where name in ('Data Entry Operator');
insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Assessment-commonSearch'), id from eg_role where name in ('Data Entry Operator');