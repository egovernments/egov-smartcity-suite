------Inert into eg_action ----------------------
Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) 
values(nextval('SEQ_EG_ACTION'),'New-LCDisposal','/legalcasedisposal/new/',(select id from eg_module where name='LCMS Transactions' ),1,
'Create Closure',false,'lcms',(select id from eg_module where name='LCMS' and parentmodule is null));
Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) 
values(nextval('SEQ_EG_ACTION'),'Edit-LCDisposal','/legalcasedisposal/edit/',(select id from eg_module where name='LCMS Transactions' ),2,
'Edit Closure',false,'lcms',(select id from eg_module where name='LCMS' and parentmodule is null));

Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='New-LCDisposal'));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Edit-LCDisposal'));


----Insert into egw status for Close Case
Insert into egw_status (id,moduletype,description,lastmodifieddate,code,order_id )
values(nextval('seq_egw_status'),'Legal Case','Close Case',now(),'CLOSE_CASE',5);

