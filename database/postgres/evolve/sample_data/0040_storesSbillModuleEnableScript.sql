#UP


update EG_MODULE set isenabled=1 where module_name='Supplier Bill';
update EG_ACTION set is_enabled=1 where  module_id in(select id_module from EG_MODULE where module_name='Supplier Bill') and name='Create Supplier Bill';
commit;

#DOWN
