------Inert into eg_action ----------------------
Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) 
values(nextval('SEQ_EG_ACTION'),'New-Hearing','/hearing/new/',(select id from eg_module where name='LCMS Transactions' ),2,
'Create Hearing',false,'lcms',(select id from eg_module where name='LCMS' and parentmodule is null));
Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) 
values(nextval('SEQ_EG_ACTION'),'Edit-Hearing','/hearing/edit/',(select id from eg_module where name='LCMS Transactions' ),1,'Edit Hearing',false,'lcms',
(select id from eg_module where name='LCMS' and parentmodule is null));

Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Edit-Hearing'));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='New-Hearing'));
------ALTER modify size of column
 ALTER TABLE eglc_hearings ALTER COLUMN purposeofhearing type varchar(1024);
 ALTER TABLE eglc_hearings ALTER COLUMN hearingoutcome type varchar(2056);
 
 ----Insert into egw status for Hearing
 Insert into egw_status (id,moduletype,description,lastmodifieddate,code,order_id )
values(nextval('seq_egw_status'),'Legal Case','In Progress',now(),'Hearing',2);

----Insert into  eg_action for employee ajax controller-----
Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application)
 values(nextval('SEQ_EG_ACTION'),'legalajaxforemployeeposition','/ajax/positions',(select id from eg_module 
 where name='LCMS Transactions'),1,'legalajaxforemployeeposition',false,'lcms',(select id from eg_module where name='LCMS' 
 and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),
(select id from eg_action where name='legalajaxforemployeeposition'));