--search boundary roleaction for pgr admin
insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='SearchBoundaryForm'),(Select id from eg_role where name='Grivance Administrator'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='SearchBoundaryForm' and context_root='pgr') and roleid='Grivance Administrator';

-- submodules for jursidiction
INSERT INTO eg_module(id_module, module_name, lastupdatedtimestamp, isenabled, module_namelocal, baseurl, parentid, module_desc, order_num) VALUES (nextval('seq_modulemaster'), 'Boundary Type', null, 1, 'Boundary Type',null,(select id_module from eg_module where module_name='Boundary Module'), 'Boundary Type', 3);

update eg_action set display_name='Create Boundary Type',module_id=(select id_module from eg_module where module_name='Boundary Type') where name='CreateBoundaryTypeForm';

update eg_action set module_id=(select id_module from eg_module where module_name='Boundary Type') where name='ViewBoundaryTypeForm';

update eg_action set module_id=(select id_module from eg_module where module_name='Boundary Type') where name='UpdateBoundaryTypeForm';

update eg_action set module_id=(select id_module from eg_module where module_name='Boundary Type') where name='AddChildBoundaryType';


INSERT INTO eg_module(id_module, module_name, lastupdatedtimestamp, isenabled, module_namelocal, baseurl, parentid, module_desc, order_num) VALUES (nextval('seq_modulemaster'), 'Hierarchy Type', null, 1, 'Hierarchy Type',null,(select id_module from eg_module where module_name='Boundary Module'), 'Hierarchy Type', 3);


update eg_action set module_id=(select id_module from eg_module where module_name='Hierarchy Type') where name='CreateHierarchyTypeForm';

update eg_action set module_id=(select id_module from eg_module where module_name='Hierarchy Type') where name='UpdateHierarchyTypeForm';

update eg_action set module_id=(select id_module from eg_module where module_name='Hierarchy Type') where name='ViewHierarchyTypeForm';

INSERT INTO eg_module(id_module, module_name, lastupdatedtimestamp, isenabled, module_namelocal, baseurl, parentid, module_desc, order_num) VALUES (nextval('seq_modulemaster'), 'Boundary', null, 1, 'Boundary',null,(select id_module from eg_module where module_name='Boundary Module'), 'Boundary', 3);

update eg_action set module_id=(select id_module from eg_module where module_name='Boundary') where name='SearchBoundaryForm';

-- submodules for pgr masters

INSERT INTO eg_module(id_module, module_name, lastupdatedtimestamp, isenabled, module_namelocal, baseurl, parentid, module_desc, order_num) VALUES (nextval('seq_modulemaster'), 'Router', null, 1, 'Router',null,(select id_module from eg_module where module_name='Pgr Masters'), 'Router', 3);

update eg_action set module_id=(select id_module from eg_module where module_name='Router') where name='Create Router';
update eg_action set module_id=(select id_module from eg_module where module_name='Router') where name='Edit Router';
update eg_action set module_id=(select id_module from eg_module where module_name='Router') where name='View Router';


INSERT INTO eg_module(id_module, module_name, lastupdatedtimestamp, isenabled, module_namelocal, baseurl, parentid, module_desc, order_num) VALUES (nextval('seq_modulemaster'), 'Complaint Type', null, 1, 'Complaint Type',null,(select id_module from eg_module where module_name='Pgr Masters'), 'Complaint Type', 3);

update eg_action set module_id=(select id_module from eg_module where module_name='Complaint Type') where name='Add Complaint Type';
update eg_action set module_id=(select id_module from eg_module where module_name='Complaint Type') where name='UpdateComplaintType';
update eg_action set module_id=(select id_module from eg_module where module_name='Complaint Type') where name='ViewComplaintType';



