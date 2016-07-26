alter table eglc_legalcase_dept add  column version bigint; 
alter table EGLC_LEGALCASE_BATCHCASE add  column version bigint; 
alter table EGLC_LEGALCASE_ADVOCATE add  column version bigint; 

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application)
 values(nextval('SEQ_EG_ACTION'),'legalajaxforposition','/legalcase/ajax/positions',(select id from eg_module 
 where name='LCMS Transactions'),1,'legalajaxforposition',false,'lcms',(select id from eg_module where name='LCMS' 
 and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),
(select id from eg_action where name='legalajaxforposition'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application)
 values(nextval('SEQ_EG_ACTION'),'legalajaxforDepartment','/legalcase/ajax/departments',(select id from eg_module 
 where name='LCMS Transactions'),1,'legalajaxforDepartment',false,'lcms',(select id from eg_module where name='LCMS' 
 and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),
(select id from eg_action where name='legalajaxforDepartment'));
