update eg_action set enabled=true where name ='Create Meeting invitation';

update eg_action set url ='/councilmeeting/agendasearch/view' where name='Create Meeting invitation';

INSERT into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'New-CouncilMeeting','/councilmeeting/new',(select id from eg_module where name='Council Meeting' and parentmodule=(select id from eg_module where name='Council Management Transaction')),2,'New-CouncilMeeting',false,'council',(select id from eg_module where name='Council Management' and parentmodule is null));

INSERT into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='New-CouncilMeeting'));


INSERT into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and View-CouncilMeeting','/councilmeeting/search/view', (select id from eg_module where name='Council Meeting' and parentmodule=(select id from eg_module where name='Council Management Transaction')),2,'View Meeting',true,'council',(select id from eg_module where name='Council Management' and parentmodule is null));

INSERT into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and View-CouncilMeeting'));


INSERT into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and View Result-CouncilMeeting','/councilmeeting/ajaxsearch/view',(select id from eg_module where name='Council Meeting' and parentmodule=(select id from eg_module where name='Council Management Transaction')),1,'Search and View Result-councilmeeting',false,'council',(select id from eg_module where name='Council Management' and parentmodule is null));

 
INSERT into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and View Result-CouncilMeeting'));

INSERT into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and Edit-Councilmeeting','/councilmeeting/search/edit',(select id from eg_module where name='Council Meeting' and parentmodule=(select id from eg_module where name='Council Management Transaction')),3,'Update Meeting',true,'council',(select id from eg_module where name='Council Management' and parentmodule is null));


INSERT into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and Edit-Councilmeeting'));

INSERT into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and Edit Result-CouncilMeeting','/councilmeeting/ajaxsearch/edit', (select id from eg_module where name='Council Meeting' and parentmodule=(select id from eg_module where name='Council Management Transaction')), 1,'Search and Edit Result-CouncilMeeting',false,'council',(select id from eg_module where name='Council Management' and parentmodule is null));
 
INSERT into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and Edit Result-CouncilMeeting'));


INSERT into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Edit-CouncilMeeting','/councilmeeting/edit',(select id from eg_module where name='Council Meeting' and parentmodule=(select id from eg_module where name='Council Management Transaction')),1,'Edit-CouncilMeeting',false,'council',(select id from eg_module where name='Council Management' and parentmodule is null));


INSERT into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Edit-CouncilMeeting'));