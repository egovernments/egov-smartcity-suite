INSERT INTO eg_module(id_module, module_name, lastupdatedtimestamp, isenabled, module_namelocal, baseurl, parentid, module_desc, order_num) VALUES (nextval('SEQ_MODULEMASTER'), 'Asset Management', now(), 1, null, 'egassets', null, 'Asset Management', null);
INSERT INTO eg_module(id_module, module_name, lastupdatedtimestamp, isenabled, module_namelocal, baseurl, parentid, module_desc, order_num) VALUES (nextval('SEQ_MODULEMASTER'), 'Asset Category', now(), 1, null, null, (select id_module from eg_module where module_name='Asset Management'), 'Asset Category', 1);
INSERT INTO eg_module(id_module, module_name, lastupdatedtimestamp, isenabled, module_namelocal, baseurl, parentid, module_desc, order_num) VALUES (nextval('SEQ_MODULEMASTER'), 'Asset Master', now(), 1, null, null, (select id_module from eg_module where module_name='Asset Management'), 'Asset Master', 1);

--rollback delete from eg_module where module_name in ('Asset Master','Asset Category','Asset Management');
