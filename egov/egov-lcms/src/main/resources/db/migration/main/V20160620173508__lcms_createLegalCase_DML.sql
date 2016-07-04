
alter table EGLC_legalcase drop column isrespondentgovernment ;

alter table eglc_bipartisandetails add  column version bigint; 


INSERT INTO eg_module (id, name, enabled, contextroot, parentmodule, displayname, ordernumber) 
VALUES (nextval('SEQ_EG_MODULE'), 'LCMS Transactions', true, NULL,(select id from eg_module where name='LCMS'), 
'Legal Case Processing', 5);

-------Inserting eg_action ------
Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application)
 values(nextval('SEQ_EG_ACTION'),'createlegalcase','/application/create/',(select id from eg_module 
 where name='LCMS Transactions'),1,'Create Legal Case',true,'lcms',(select id from eg_module where name='LCMS' 
 and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),
(select id from eg_action where name='createlegalcase'));