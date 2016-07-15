---Insert eg_action for Edit Judgment
Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) 
values(nextval('SEQ_EG_ACTION'),'Edit-Judgment','/judgment/edit/',(select id from eg_module where name='LCMS Transactions' ),1,'Edit Judgment',false,'lcms',
(select id from eg_module where name='LCMS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Edit-Judgment'));

----Insert into egw status for Judgment
Insert into egw_status (id,moduletype,description,lastmodifieddate,code,order_id )
values(nextval('seq_egw_status'),'Legal Case','Judgment Created',now(),'JUDGMENT',1);