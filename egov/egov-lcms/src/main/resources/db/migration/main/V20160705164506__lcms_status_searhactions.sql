delete from egw_status where moduletype='LCMS' and code='LCCREATED';

insert into egw_status (id,moduletype,description,lastmodifieddate,code,order_id )
values(nextval('seq_egw_status'),'Legal Case','Created','2016-06-27 13:10:10.622846','LCCREATED',1);


Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application)
 values(nextval('SEQ_EG_ACTION'),'searchlegalcase','/search/searchForm/',(select id from eg_module 
 where name='LCMS Transactions'),1,'Search Legal Case',true,'lcms',(select id from eg_module where name='LCMS' 
 and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),
(select id from eg_action where name='searchlegalcase'));