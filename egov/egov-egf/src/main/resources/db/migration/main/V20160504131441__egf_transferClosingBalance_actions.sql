Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'TransferClosingBalance','/pea/transferClosingBalance-new.action',(select id from eg_module where name='Period End Activities' ),1,'Transfer Closing Balance',true,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));

Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='TransferClosingBalance'));


Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'TransferClosingBalanceTransfer','/pea/transferClosingBalance-transfer.action',(select id from eg_module where name='Period End Activities' ),1,'Transfer Closing Balance',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));

Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='TransferClosingBalanceTransfer'));
