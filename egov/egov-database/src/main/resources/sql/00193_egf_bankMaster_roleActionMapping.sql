------------------- Bank Master (save and modify egAction)  ---------------

Insert into eg_action(id,name,url,module_id,order_number,is_enabled,context_root) 
values(nextval('SEQ_EG_ACTION'),'saveBank','/masters/bank-save.action',
(select id_module from eg_module where module_name='Chart of Accounts'),1,0,'EGF');

Insert into eg_roleaction_map  values((select id from eg_role where name='Super User'),(select id from eg_Action where name='saveBank'));


Insert into eg_action(id,name,url,module_id,order_number,is_enabled,context_root) 
values(nextval('SEQ_EG_ACTION'),'modifyBank','/masters/bank-execute.action',
(select id_module from eg_module where module_name='Chart of Accounts'),1,0,'EGF');

Insert into eg_roleaction_map  values((select id from eg_role where name='Super User'),(select id from eg_Action where name='modifyBank'));


