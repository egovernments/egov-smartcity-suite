Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot) 
values(nextval('SEQ_EG_ACTION'),'BillRegisterSearch','/bill/billRegisterSearch-search',
null,1,'BillRegisterSearch',false,'EGF');

Insert into eg_roleaction   values((select id from eg_role where name='Super User'),(select id from eg_action where name='BillRegisterSearch'));


