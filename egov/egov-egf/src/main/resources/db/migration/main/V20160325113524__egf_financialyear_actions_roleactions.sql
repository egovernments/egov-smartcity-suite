insert into EG_MODULE (ID,NAME,ENABLED,CONTEXTROOT,PARENTMODULE,DISPLAYNAME,ORDERNUMBER)
VALUES (NEXTVAL('SEQ_EG_MODULE'),'Financial Masters Financial year','true',null,
(select id from eg_module where name='Masters' and parentmodule=(select id from eg_module where name='EGF' and parentmodule is null)),
'Financial Year', 9);

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'New-CFinancialYear','/cfinancialyear/new',(select id from eg_module where name='Financial Masters Financial year' ),1,'Create FinancialYear',true,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='New-CFinancialYear'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Create-CFinancialYear','/cfinancialyear/create',(select id from eg_module where name='Financial Masters Financial year'),1,'Create-CFinancialYear',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Create-CFinancialYear'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Update-CFinancialYear','/cfinancialyear/update',(select id from eg_module where name='Financial Masters Financial year' ),1,'Update-CFinancialYear',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Update-CFinancialYear'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'View-CFinancialYear','/cfinancialyear/view',(select id from eg_module where name='Financial Masters Financial year' ),1,'View-CFinancialYear',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='View-CFinancialYear'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Edit-CFinancialYear','/cfinancialyear/edit',(select id from eg_module where name='Financial Masters Financial year'),1,'Edit-CFinancialYear',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Edit-CFinancialYear'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Result-CFinancialYear','/cfinancialyear/result',(select id from eg_module where name='Financial Masters Financial year'),1,'Result-CFinancialYear',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Result-CFinancialYear'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and View-CFinancialYear','/cfinancialyear/search/view',(select id from eg_module where name='Financial Masters Financial year'),2,'View FinancialYear',true,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and View-CFinancialYear'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and Edit-CFinancialYear','/cfinancialyear/search/edit',(select id from eg_module where name='Financial Masters Financial year'),3,'Edit FinancialYear',true,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and Edit-CFinancialYear'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and View Result-CFinancialYear','/cfinancialyear/ajaxsearch/view',(select id from eg_module where name='Financial Masters Financial year'),1,'Search and View Result-CFinancialYear',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and View Result-CFinancialYear'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and Edit Result-CFinancialYear','/cfinancialyear/ajaxsearch/edit',(select id from eg_module where name='Financial Masters Financial year'),1,'Search and Edit Result-CFinancialYear',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and Edit Result-CFinancialYear'));

