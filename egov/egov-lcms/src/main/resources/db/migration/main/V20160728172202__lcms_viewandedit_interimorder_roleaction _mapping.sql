--update the status description for judgment
update egw_status set description='Judgment' where code='JUDGMENT';


----Inert into eg_action
Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application)
values(nextval('SEQ_EG_ACTION'),'View LcInterimOrder','/lcinterimorder/view/',(select id from eg_module 
where name='LCMS Transactions'),1,'View LcInterimOrder',false,'lcms',(select id from eg_module where name='LCMS' 
and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),
(select id from eg_action where name='View LcInterimOrder'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application)
 values(nextval('SEQ_EG_ACTION'),'List LcInterimOrder','/lcinterimorder/list/',(select id from eg_module 
 where name='LCMS Transactions'),1,'List LcInterimOrder',false,'lcms',(select id from eg_module where name='LCMS' 
 and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),
(select id from eg_action where name='List LcInterimOrder'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application)
values(nextval('SEQ_EG_ACTION'),'Edit LcInterimOrder','/lcinterimorder/edit/',(select id from eg_module 
where name='LCMS Transactions'),1,'Edit LcInterimOrder',false,'lcms',(select id from eg_module where name='LCMS' 
and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),
(select id from eg_action where name='Edit LcInterimOrder'));


