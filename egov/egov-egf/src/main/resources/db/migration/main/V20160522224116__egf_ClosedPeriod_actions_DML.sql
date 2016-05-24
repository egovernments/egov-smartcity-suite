

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Update-ClosedPeriod','/closedperiod/update',(select id from eg_module where name='Masters' and parentmodule=(select id from eg_module where name='EGF' and parentmodule is null)),1,'Update-ClosedPeriod',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Update-ClosedPeriod'));
Insert into eg_roleaction values((select id from eg_role where name='Financial Administrator'),(select id from eg_action where name='Update-ClosedPeriod'));


Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Edit-ClosedPeriod','/closedperiod/edit',(select id from eg_module where name='Masters' and parentmodule=(select id from eg_module where name='EGF' and parentmodule is null)),1,'Edit-ClosedPeriod',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Edit-ClosedPeriod'));
Insert into eg_roleaction values((select id from eg_role where name='Financial Administrator'),(select id from eg_action where name='Edit-ClosedPeriod'));


Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Result-ClosedPeriod','/closedperiod/result',(select id from eg_module where name='Masters' and parentmodule=(select id from eg_module where name='EGF' and parentmodule is null)),1,'Result-ClosedPeriod',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Result-ClosedPeriod'));
Insert into eg_roleaction values((select id from eg_role where name='Financial Administrator'),(select id from eg_action where name='Result-ClosedPeriod'));


Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and Edit-ClosedPeriod','/closedperiod/search/edit',(select id from eg_module where name='Period End Activities' and parentmodule=(select id from eg_module where name='EGF' and parentmodule is null)),3,'Close Period',true,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and Edit-ClosedPeriod'));
Insert into eg_roleaction values((select id from eg_role where name='Financial Administrator'),(select id from eg_action where name='Search and Edit-ClosedPeriod'));


Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and Edit Result-ClosedPeriod','/closedperiod/ajaxsearch/edit',(select id from eg_module where name='Masters' and parentmodule=(select id from eg_module where name='EGF' and parentmodule is null)),1,'Search and Edit Result-ClosedPeriod',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and Edit Result-ClosedPeriod'));
Insert into eg_roleaction values((select id from eg_role where name='Financial Administrator'),(select id from eg_action where name='Search and Edit Result-ClosedPeriod'));


