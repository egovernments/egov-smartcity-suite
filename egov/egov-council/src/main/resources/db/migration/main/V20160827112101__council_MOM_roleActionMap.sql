
INSERT into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Update Meeting','/councilmeeting/update',(select id from eg_module where name='Council Meeting' and parentmodule=(select id from eg_module where name='Council Management Transaction')),1,'Update Meeting',false,'council',(select id from eg_module where name='Council Management' and parentmodule is null));

INSERT into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Update Meeting'));

INSERT into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'New-CouncilMOM','/councilmom/new',(select id from eg_module where name='Council Meeting' and parentmodule=(select id from eg_module where name='Council Management Transaction')),4,'Create MOM',false,'council',(select id from eg_module where name='Council Management' and parentmodule is null));

INSERT into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='New-CouncilMOM'));

INSERT into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Create-CouncilMOM','/councilmom/meetingsearch/view',(select id from eg_module where name='Council Meeting' and parentmodule=(select id from eg_module where name='Council Management Transaction')),4,'Create Council MOM',true,'council',(select id from eg_module where name='Council Management' and parentmodule is null));

update eg_action set url ='/councilmom/meetingsearch/view' where name='Create-CouncilMOM';

INSERT into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Create-CouncilMOM'));

INSERT into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Create-MOM','/councilmom/create',(select id from eg_module where name='Council Meeting' and parentmodule=(select id from eg_module where name='Council Management Transaction')),4,'Create-MOM',false,'council',(select id from eg_module where name='Council Management' and parentmodule is null));

INSERT into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Create-MOM'));

INSERT into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Update Mom','/councilmom/update',(select id from eg_module where name='Council Meeting' and parentmodule=(select id from eg_module where name='Council Management Transaction')),1,'Update Mom',false,'council',(select id from eg_module where name='Council Management' and parentmodule is null));

INSERT into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Update Mom'));

INSERT into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Result-MOM','/councilmom/result',(select id from eg_module where name='Council Meeting' and parentmodule=(select id from eg_module where name='Council Management Transaction')),4,'Result-MOM',false,'council',(select id from eg_module where name='Council Management' and parentmodule is null));

INSERT into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Result-MOM'));

INSERT into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and View-CouncilMOM','/councilmom/search/view',(select id from eg_module where name='Council Meeting' and parentmodule=(select id from eg_module where name='Council Management Transaction')),5,'View Council MOM',true,'council',(select id from eg_module where name='Council Management' and parentmodule is null));

INSERT into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and View-CouncilMOM'));

INSERT into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'View-CouncilMOM','/councilmom/view',(select id from eg_module where name='Council Meeting'and parentmodule=(select id from eg_module where name='Council Management Transaction')),5,'View-CouncilMOM',false,'council',(select id from eg_module where name='Council Management' and parentmodule is null));

INSERT into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='View-CouncilMOM'));

INSERT into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and Edit-CouncilMOM','/councilmom/search/edit',(select id from eg_module where name='Council Meeting' and parentmodule=(select id from eg_module where name='Council Management Transaction')),6,'Update Council MOM',true,'council',(select id from eg_module where name='Council Management' and parentmodule is null));

INSERT into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and Edit-CouncilMOM'));

INSERT into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'view-Departmentlist','/councilmom/departmentlist',(select id from eg_module where name='Council Meeting' and parentmodule=(select id from eg_module where name='Council Management Transaction')),4,'view-Departmentlist',false,'council',(select id from eg_module where name='Council Management' and parentmodule is null));
 
INSERT into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='view-Departmentlist'));

INSERT into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'view-resolutionlist','/councilmom/resolutionlist',(select id from eg_module where name='Council Meeting' and parentmodule=(select id from eg_module where name='Council Management Transaction')),4,'view-resolutionlist',false,'council',(select id from eg_module where name='Council Management' and parentmodule is null));
 
INSERT into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='view-resolutionlist'));

INSERT into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'view-wardlist','/councilmom/wardlist',(select id from eg_module where name='Council Meeting' and parentmodule=(select id from eg_module where name='Council Management Transaction')),4,'view-wardlist',false,'council',(select id from eg_module where name='Council Management' and parentmodule is null));
 
INSERT into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='view-wardlist'));