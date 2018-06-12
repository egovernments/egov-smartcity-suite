INSERT INTO eg_module (id, name, enabled, contextroot, parentmodule, displayname, ordernumber) VALUES (nextval('SEQ_EG_MODULE'), 'Survey', true, 'ptis', (select id from eg_module where name='Property Tax'), 'Survey Applications', null);

insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,
lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'SearchSurveyApplications','/survey/appSearch/applicationdetails',null,(select id from eg_module where name='Survey'),1,'Search Survey Applications',true,'ptis',0,1,now(),1,
now(),(select id from eg_module where name='Survey'));

insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'SearchSurveyApplications'),
id from eg_role where name in ('Property Verifier','Property Approver','SYSTEM');
