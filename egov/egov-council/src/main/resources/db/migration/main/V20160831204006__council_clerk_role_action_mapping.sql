update eg_action set url='/agenda/searchagenda/edit' where name='Search and Edit-CouncilAgenda';

INSERT into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values  (nextval('SEQ_EG_ACTION'),'SearchCreatedMOM View','/councilmom/searchcreated-mom/view',    (select id from eg_module where name='Council Meeting' and parentmodule=(select id from eg_module  where name='Council Management Transaction')),1,'SearchCreatedMOM View',false,'council',    (select id from eg_module where name='Council Management' and parentmodule is null));

INSERT into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values    (nextval('SEQ_EG_ACTION'),'SearchCreatedMOM Edit','/councilmom/searchcreated-mom/edit',    (select id from eg_module where name='Council Meeting' and parentmodule=(select id from eg_module  where name='Council Management Transaction')),1,'SearchCreatedMOM Edit',false,'council',    (select id from eg_module where name='Council Management' and parentmodule is null));

INSERT into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='SearchCreatedMOM View'));
INSERT into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='SearchCreatedMOM Edit'));

-- New Role council clerk added.
INSERT INTO eg_role(id, name, description, createddate, createdby, lastmodifiedby, lastmodifieddate, version) VALUES (nextval('seq_eg_role'), 'Council Clerk', 'Council Clerk', now(), 1, 1, now(), 0);

-- Roleaction for council clerk.
INSERT into EG_ROLEACTION(ROLEID,ACTIONID) 
select (select id from eg_role where name='Council Clerk'), id from eg_action where name in ('New Agenda','AgendaAjaxSearch','createAgenda',
	'Result-Agenda','Search and Edit-CouncilAgenda','SearchAgendaToCreateMeeting','Edit-CouncilAgenda','Result-Agenda',
	'Update Agenda','Search and View-CouncilAgenda','View-CouncilAgenda','Search and View Result-CouncilAgenda',
	'New-CouncilMeeting','Create Meeting invitation','SearchAgendaToCreateMeeting','Save Meeting invitation',
	'Result-CouncilMeeting','Search and Edit-Councilmeeting','Search and Edit Result-CouncilMeeting','Edit-CouncilMeeting',
	'Update Meeting','Search and View-CouncilMeeting','Search and View Result-CouncilMeeting','View-CouncilMeeting',
	'Create-CouncilMOM','SearchMeetingToCreateMOM','New-CouncilMOM','view-wardlist','view-resolutionlist',
	'view-Departmentlist','Create-MOM','Result-MOM','Search and Edit-CouncilMOM','SearchCreatedMOM Edit','Update Mom',
	'Search and View-CouncilMOM','SearchCreatedMOM View','View-CouncilMOM','SearchAttendanceForMeeting','ShowAttendanceSearchResult',
	'AttendanceAjaxSearch','RetrieveSmsAndEmailForMeeting','SearchMeetingToCreateMOM','SendSmsAndEmailForMeeting','Attendance Report');

