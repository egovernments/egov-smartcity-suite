
create sequence SEQ_EGCNCL_MEETING_NUMBER;

Insert into egw_status (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (nextval('SEQ_EGW_STATUS'),'COUNCILMEETING','CREATED',now(),'CREATED',1);

alter table egcncl_meeting_mom alter column resolutiondetail drop not null;

INSERT INTO eg_module(id, name, enabled, contextroot, parentmodule, displayname, ordernumber)  VALUES (nextval('SEQ_EG_MODULE'), 'Council Meeting', true, 'council', (select id from eg_module where name='Council Management Transaction'), 'Council Meeting', 3);

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Create Meeting invitation','/councilmeeting/new',(select id from eg_module where name='Council Meeting' and parentmodule=(select id from eg_module where name='Council Management Transaction' )),1, 'Create Meeting invitation',false,'council',(select id from eg_module where name='Council Management' and parentmodule is null));
Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Save Meeting invitation','/councilmeeting/create',(select id from eg_module where name='Council Meeting' and parentmodule=(select id from eg_module where name='Council Management Transaction' )),1, 'Save Meeting invitation',false,'council',(select id from eg_module where name='Council Management' and parentmodule is null));
Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Result-CouncilMeeting','/councilmeeting/result',(select id from eg_module where name='Council Meeting' and parentmodule=(select id from eg_module where name='Council Management Transaction' )),1,'Result-CouncilMeeting',false,'council',(select id from eg_module where name='Council Management' and parentmodule is null));
Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'View-CouncilMeeting','/councilmeeting/view',(select id from eg_module where name='Council Meeting' and parentmodule=(select id from eg_module where name='Council Management Transaction' )),4,'View Council Meeting',false,'council',(select id from eg_module where name='Council Management' and parentmodule is null));

Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='View-CouncilMeeting'));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Save Meeting invitation'));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Create Meeting invitation'));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Result-CouncilMeeting'));
