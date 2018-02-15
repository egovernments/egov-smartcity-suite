INSERT INTO eg_module(id, name, enabled, contextroot, parentmodule, displayname, ordernumber)
    VALUES (nextval('SEQ_EG_MODULE'), 'Council Router', true, 'council', (select id from eg_module where name='Council Management Master'), 'Council Router', 4);


Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'CouncilRouter','/councilrouter/new',(select id from eg_module where name='Council Router' and parentmodule=(select id from eg_module where name='Council Management Master' )),1,'Create Council Router',true,'council',(select id from eg_module where name='Council Management' and parentmodule is null));

Insert into eg_roleaction values((select id from eg_role where name='SYSTEM'),(select id from eg_action where name='CouncilRouter'));
Insert into eg_roleaction values((select id from eg_role where name='Council Management Admin'),(select id from eg_action where name='CouncilRouter'));


Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'CreateCouncilRouter','/councilrouter/create',(select id from eg_module where name='Council Router' and parentmodule=(select id from eg_module where name='Council Management Master')),1,null,false,'council',(select id from eg_module where name='Council Management' and parentmodule is null));

Insert into eg_roleaction values((select id from eg_role where name='SYSTEM'),(select id from eg_action where name='CreateCouncilRouter'));
Insert into eg_roleaction values((select id from eg_role where name='Council Management Admin'),(select id from eg_action where name='CreateCouncilRouter'));



Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'CouncilRouterResult','/councilrouter/result',(select id from eg_module where name='Council Router' and parentmodule=(select id from eg_module where name='Council Management Master' )),1,null,false,'council',(select id from eg_module where name='Council Management' and parentmodule is null));

Insert into eg_roleaction values((select id from eg_role where name='SYSTEM'),(select id from eg_action where name='CouncilRouterResult'));
Insert into eg_roleaction values((select id from eg_role where name='Council Management Admin'),(select id from eg_action where name='CouncilRouterResult'));



Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'CouncilRouterUpdate','/councilrouter/update',(select id from eg_module where name='Council Router' and parentmodule=(select id from eg_module where name='Council Management Master')),1,null,false,'council',(select id from eg_module where name='Council Management' and parentmodule is null));

Insert into eg_roleaction values((select id from eg_role where name='SYSTEM'),(select id from eg_action where name='CouncilRouterUpdate'));
Insert into eg_roleaction values((select id from eg_role where name='Council Management Admin'),(select id from eg_action where name='CouncilRouterUpdate'));


Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'CouncilRouterView','/councilrouter/view',(select id from eg_module where name='Council Router' and parentmodule=(select id from eg_module where name='Council Management Master')),1,null,false,'council',(select id from eg_module where name='Council Management' and parentmodule is null));

Insert into eg_roleaction values((select id from eg_role where name='SYSTEM'),(select id from eg_action where name='CouncilRouterView'));
Insert into eg_roleaction values((select id from eg_role where name='Council Management Admin'),(select id from eg_action where name='CouncilRouterView'));

Insert into eg_action(id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'CouncilRouterSearchEdit','/councilrouter/search/edit','mode=edit',(select id from eg_module where name='Council Router' and parentmodule=(select id from eg_module where name='Council Management Master')),2,'Update Council Router',true,'council',(select id from eg_module where name='Council Management' and parentmodule is null));

Insert into eg_roleaction values((select id from eg_role where name='SYSTEM'),(select id from eg_action where name='CouncilRouterSearchEdit'));
Insert into eg_roleaction values((select id from eg_role where name='Council Management Admin'),(select id from eg_action where name='CouncilRouterSearchEdit'));


Insert into eg_action(id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'CouncilRouterSearchView','/councilrouter/search/view','mode=view',(select id from eg_module where name='Council Router' and parentmodule=(select id from eg_module where name='Council Management Master' )),3,'View Council Router',true,'council',(select id from eg_module where name='Council Management' and parentmodule is null));

Insert into eg_roleaction values((select id from eg_role where name='SYSTEM'),(select id from eg_action where name='CouncilRouterSearchView'));
Insert into eg_roleaction values((select id from eg_role where name='Council Management Admin'),(select id from eg_action where name='CouncilRouterSearchView'));


Insert into eg_action(id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'CouncilRouterAjaxEdit','/councilrouter/ajaxsearch/edit',null,(select id from eg_module where name='Council Router' and parentmodule=(select id from eg_module where name='Council Management Master' )),3,null,false,'council',(select id from eg_module where name='Council Management' and parentmodule is null));

Insert into eg_roleaction values((select id from eg_role where name='SYSTEM'),(select id from eg_action where name='CouncilRouterAjaxEdit'));
Insert into eg_roleaction values((select id from eg_role where name='Council Management Admin'),(select id from eg_action where name='CouncilRouterAjaxEdit'));


Insert into eg_action(id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'CouncilRouterAjaxView','/councilrouter/ajaxsearch/view',null,(select id from eg_module where name='Council Router' and parentmodule=(select id from eg_module where name='Council Management Master' )),3,null,false,'council',(select id from eg_module where name='Council Management' and parentmodule is null));

Insert into eg_roleaction values((select id from eg_role where name='SYSTEM'),(select id from eg_action where name='CouncilRouterAjaxView'));
Insert into eg_roleaction values((select id from eg_role where name='Council Management Admin'),(select id from eg_action where name='CouncilRouterAjaxView'));


Insert into eg_action(id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'CouncilRouterEdit','/councilrouter/edit',null,(select id from eg_module where name='Council Router' and parentmodule=(select id from eg_module where name='Council Management Master' )),3,null,false,'council',(select id from eg_module where name='Council Management' and parentmodule is null));

Insert into eg_roleaction values((select id from eg_role where name='SYSTEM'),(select id from eg_action where name='CouncilRouterEdit'));
Insert into eg_roleaction values((select id from eg_role where name='Council Management Admin'),(select id from eg_action where name='CouncilRouterEdit'));


