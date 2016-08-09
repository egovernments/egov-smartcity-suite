--------------------Insert into  eg_action 
Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) 
values(nextval('SEQ_EG_ACTION'),'view-Hearing','/hearing/list/',(select id from eg_module where name='LCMS Transactions' ),1,'view Hearing',false,'lcms',
(select id from eg_module where name='LCMS' and parentmodule is null));

Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='view-Hearing'));