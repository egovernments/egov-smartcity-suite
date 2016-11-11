
update eg_action set url='/councilmeeting/attend/search/edit' where name='Edit Attendance';

--council Masters view role-actions
INSERT INTO EG_ROLEACTION (roleid,actionid) (SELECT (select id from eg_role where name='COUNCIL_VIEW_ACCESS_ROLE') as roleid, id from eg_action where name in ('Search and View-CouncilCommittee','Search and View Result-CouncilCommitteetype','View-CouncilCommittee') and contextroot = 'council' );

--council Transaction view role-actions
INSERT INTO EG_ROLEACTION (roleid,actionid) (SELECT (select id from eg_role where name='COUNCIL_VIEW_ACCESS_ROLE') as roleid, id from eg_action where name in ('Search and View-CouncilPreamble','Search and View Result-CouncilPreamble','View-CouncilPreamble','Download-documnets','Search and View-CouncilAgenda','Search and View Result-CouncilAgenda','View-CouncilAgenda','Search and View-CouncilMeeting','Search and View Result-CouncilMeeting','View-CouncilMeeting','View-CouncilMOM','view-Departmentlist','view-resolutionlist','view-wardlist','SearchCreatedMOM View','DownloadFinalResolutionPdf','Search and View-CouncilMOM','Show Attendance Result','GenerateAgendaPdf','generateresolutionForMom','generate-resolution','SearchAttendanceForMeeting','AttendanceAjaxSearch') and contextroot ='council');

--council Reports role-actions
INSERT INTO EG_ROLEACTION (roleid,actionid) (SELECT (select id from eg_role where name='COUNCIL_VIEW_ACCESS_ROLE') as roleid, id from eg_action where name in ('Attendance Report','Ward wise Preamble Report','Preamble Wardwise Search Result','ShowAttendanceSearchResult') and contextroot = 'council' );