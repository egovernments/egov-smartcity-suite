INSERT into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) 
values(nextval('SEQ_EG_ACTION'),'Result-Agenda','/agenda/result',(select id from eg_module where name='Council Agenda' and 
parentmodule=(select id from eg_module where name='Council Management Transaction')),1,'Result-Agenda',false,'council',
(select id from eg_module where name='Council Management' and parentmodule is null));

INSERT into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Result-Agenda'));

INSERT into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application)
 values(nextval('SEQ_EG_ACTION'),'Search and View-CouncilAgenda','/agenda/search/view',
 (select id from eg_module where name='Council Agenda' 
and parentmodule=(select id from eg_module where name='Council Management Transaction')),2,'View Agenda',true,'council',
(select id from eg_module where name='Council Management' and parentmodule is null));


INSERT into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and View-CouncilAgenda'));

INSERT into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) 
values(nextval('SEQ_EG_ACTION'),'Search and View Result-CouncilAgenda','/agenda/ajaxsearch/view',
(select id from eg_module where name='Council Agenda' and parentmodule=(select id from eg_module where name='Council Management Transaction'))
,1,'Search and View Result-CouncilAgenda',false,'council',(select id from eg_module where name='Council Management' and parentmodule is null));

 
INSERT into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and View Result-CouncilAgenda'));

INSERT into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) 
values(nextval('SEQ_EG_ACTION'),'View-CouncilAgenda','/agenda/view',(select id from eg_module where name='Council Agenda'
 and parentmodule=(select id from eg_module where name='Council Management Transaction')),1,'View-CouncilAgenda',false,'council',
 (select id from eg_module where name='Council Management' and parentmodule is null));


INSERT into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='View-CouncilAgenda'));

INSERT into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application)
 values(nextval('SEQ_EG_ACTION'),'Update Agenda','/agenda/update',(select id from eg_module where name='Council Agenda' 
and parentmodule=(select id from eg_module where name='Council Management Transaction')),1,'Update Agenda',false,'council',
(select id from eg_module where name='Council Management' and parentmodule is null));


INSERT into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Update Agenda'));


INSERT into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) 
values(nextval('SEQ_EG_ACTION'),'Search and Edit-CouncilAgenda','/agenda/search/edit',(select id from eg_module where name='Council Agenda' 
and parentmodule=(select id from eg_module where name='Council Management Transaction')),3,'Update Agenda',true,'council',
(select id from eg_module where name='Council Management' and parentmodule is null));


INSERT into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and Edit-CouncilAgenda'));

INSERT into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application)
 values(nextval('SEQ_EG_ACTION'),'Search and Edit Result-CouncilAgenda','/agenda/ajaxsearch/edit',
 (select id from eg_module where name='Council Agenda' and parentmodule=(select id from eg_module where name='Council Management Transaction')),
 1,'Search and Edit Result-CouncilAgenda',false,'council',(select id from eg_module where name='Council Management' and parentmodule is null));
 
INSERT into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and Edit Result-CouncilAgenda'));


INSERT into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) 
values(nextval('SEQ_EG_ACTION'),'Edit-CouncilAgenda','/agenda/edit',
(select id from eg_module where name='Council Agenda' and parentmodule=
(select id from eg_module where name='Council Management Transaction'))
,1,'Edit-CouncilAgenda',false,'council',
(select id from eg_module where name='Council Management' and parentmodule is null));


INSERT into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Edit-CouncilAgenda'));

