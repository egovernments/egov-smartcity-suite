insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'SurveyApplicationsSearchForm',
'/survey/properties/searchform',null,(select id from eg_module where name='Survey'),1,'Survey Applications Workflow',true,'ptis',0,1,now(),1,now(),(select id from eg_module where name='Survey'));

insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'SurveyApplicationsSearch',
'/survey/properties/search',null,(select id from eg_module where name='Survey'),1,'Survey Applications Search ',false,'ptis',0,1,now(),1,now(),(select id from eg_module where name='Survey'));

insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'SurveyApplicationsUpdateWorkflow',
'/survey/properties/updateworkflow',null,(select id from eg_module where name='Survey'),1,'Survey Applications Update Workflow',false,'ptis',0,1,now(),1,now(),(select id from eg_module where name='Survey'));
