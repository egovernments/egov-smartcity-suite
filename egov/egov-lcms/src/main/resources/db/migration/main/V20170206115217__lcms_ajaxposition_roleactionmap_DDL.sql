----Insert into  eg_action for  ajax controller-----
Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application)
 values(nextval('SEQ_EG_ACTION'),'legalajaxposition','/ajax/getposition',(select id from eg_module 
 where name='LCMS Transactions'),1,'legalajaxposition',false,'lcms',(select id from eg_module where name='LCMS' 
 and parentmodule is null));
 
Insert into eg_roleaction values((select id from eg_role where name='Super User'),
(select id from eg_action where name='legalajaxposition'));
