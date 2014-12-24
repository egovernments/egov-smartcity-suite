#UP
-----Action Update DML---
DELETE FROM EG_ROLEACTION_MAP WHERE ACTIONID IN (SELECT ID FROM EG_ACTION WHERE CONTEXT_ROOT='pgr');
DELETE FROM EG_ACTION WHERE CONTEXT_ROOT='pgr';
DELETE FROM EG_MODULE WHERE BASEURL='pgr';
INSERT INTO EG_MODULE VALUES(SEQ_MODULEMASTER.NEXTVAL, 'PGR', sysdate, 1, 'Public Grievances', 'pgr', NULL, 'Public Grievances', 1);
INSERT INTO EG_MODULE VALUES(SEQ_MODULEMASTER.NEXTVAL, 'PGRComplaint', sysdate, 1, 'Complaint', 'pgr', (SELECT ID_MODULE from EG_MODULE where MODULE_NAME = 'PGR'), 'Complaint', 1);
INSERT INTO EG_MODULE VALUES(SEQ_MODULEMASTER.NEXTVAL, 'PGRMasters', sysdate, 1, 'Masters', 'pgr', (SELECT ID_MODULE from EG_MODULE where MODULE_NAME = 'PGR'), 'Masters', 2);
INSERT INTO EG_MODULE VALUES(SEQ_MODULEMASTER.NEXTVAL, 'PGRReports', sysdate, 1, 'Reports', 'pgr', (SELECT ID_MODULE from EG_MODULE where MODULE_NAME = 'PGR'), 'Reports', 3);

INSERT INTO EG_ACTION VALUES (SEQ_EG_ACTION.NEXTVAL, 'PGRIndex', NULL, NULL, sysdate, '/eGov.jsp', NULL, NULL, (SELECT ID_MODULE from EG_MODULE where MODULE_NAME = 'PGR'), 1, null, 0, NULL,'pgr'); 

INSERT INTO EG_ACTION VALUES (SEQ_EG_ACTION.NEXTVAL, 'PGRComplaintRegister', NULL, NULL, sysdate, '/complaint/registerComplaint!newform.action', NULL, 1, (SELECT ID_MODULE from EG_MODULE where MODULE_NAME = 'PGRComplaint'), 1, 'Register Complaint', 1, NULL,'pgr'); 
INSERT INTO EG_ACTION VALUES (SEQ_EG_ACTION.NEXTVAL, 'PGRComplaintMyComplaints', NULL, NULL, sysdate, '/complaint/listMyComplaints!newform.action', NULL, 2, (SELECT ID_MODULE from EG_MODULE where MODULE_NAME = 'PGRComplaint'), 2, 'My Registered Complaints', 1, NULL,'pgr');
INSERT INTO EG_ACTION VALUES (SEQ_EG_ACTION.NEXTVAL, 'PGRComplaintMyCompletedComplaints', NULL, NULL, sysdate, '/complaint/listMyComplaints!newform.action', 'mode=completed', 3, (SELECT ID_MODULE from EG_MODULE where MODULE_NAME = 'PGRComplaint'), 3, 'My Completed Complaints', 1, NULL,'pgr');

INSERT INTO EG_ACTION VALUES (SEQ_EG_ACTION.NEXTVAL, 'PGRReportsByWard', NULL, NULL, sysdate, '/reports/wardWiseReport!newform.action', NULL, 1, (SELECT ID_MODULE from EG_MODULE where MODULE_NAME = 'PGRReports'), 4, 'Ward Wise', 1, NULL,'pgr'); 
INSERT INTO EG_ACTION VALUES (SEQ_EG_ACTION.NEXTVAL, 'PGRReportsByWardAggregate', NULL, NULL, sysdate, '/reports/wardWiseAggregated!newform.action', null, 1, (SELECT ID_MODULE from EG_MODULE where MODULE_NAME = 'PGRReports'), 5, 'Ward Wise Aggregate', 1, NULL,'pgr');
INSERT INTO EG_ACTION VALUES (SEQ_EG_ACTION.NEXTVAL, 'PGRReportsByDepartmentwise', NULL, NULL, sysdate, '/reports/departmentWiseReport!newform.action', NULL, 1, (SELECT ID_MODULE from EG_MODULE where MODULE_NAME = 'PGRReports'), 6, 'Department Wise', 1, NULL,'pgr');
INSERT INTO EG_ACTION VALUES (SEQ_EG_ACTION.NEXTVAL, 'PGRReportsByDepartmentAggregate', NULL, NULL, sysdate, '/reports/departmentWiseAggregated!newform.action', NULL, 1, (SELECT ID_MODULE from EG_MODULE where MODULE_NAME = 'PGRReports'), 7, 'Department Wise Aggregate', 1, NULL,'pgr');
INSERT INTO EG_ACTION VALUES (SEQ_EG_ACTION.NEXTVAL, 'PGRReportsByComplainerDetail', NULL, NULL, sysdate, '/reports/searchByNameReport!newform.action', null, 1, (SELECT ID_MODULE from EG_MODULE where MODULE_NAME = 'PGRReports'), 8, 'Complainer Detail Wise', 1, NULL,'pgr');
INSERT INTO EG_ACTION VALUES (SEQ_EG_ACTION.NEXTVAL, 'PGRReportsByComplaintGroup', NULL, NULL, sysdate, '/reports/complaintGroupWiseReport!newform.action', null, 1, (SELECT ID_MODULE from EG_MODULE where MODULE_NAME = 'PGRReports'), 9, 'Complaint Group Wise', 1, NULL,'pgr');
INSERT INTO EG_ACTION VALUES (SEQ_EG_ACTION.NEXTVAL, 'PGRReportsByChart', NULL, NULL, sysdate, '/chart/groupWiseBoundaryLevelReport!newform.action', null, 1, (SELECT ID_MODULE from EG_MODULE where MODULE_NAME = 'PGRReports'), 10, 'City Grievances Chart', 1, NULL,'pgr');
INSERT INTO EG_ACTION VALUES (SEQ_EG_ACTION.NEXTVAL, 'PGRReportsByWardWiseKML', NULL, NULL, sysdate, '/reports/wardWiseKmlReport!newform.action', null, 1, (SELECT ID_MODULE from EG_MODULE where MODULE_NAME = 'PGRReports'), 11, 'Ward Wise Google Map Overlay', 1, NULL,'pgr');
INSERT INTO EG_ACTION VALUES (SEQ_EG_ACTION.NEXTVAL, 'PGRReportsGeneric', NULL, NULL, sysdate, '/reports/complaintGenericReport!search.action', null, 1, (SELECT ID_MODULE from EG_MODULE where MODULE_NAME = 'PGRReports'), 12, 'Report Generic', 0, NULL,'pgr');

INSERT INTO EG_ACTION VALUES (SEQ_EG_ACTION.NEXTVAL, 'PGRMastersComplaintGroupNew', NULL, NULL, sysdate, '/masters/complaintGroupMaster!newform.action', NULL, 1, (SELECT ID_MODULE from EG_MODULE where MODULE_NAME = 'PGRMasters'), 13, 'Complaint Group', 1, NULL,'pgr');
INSERT INTO EG_ACTION VALUES (SEQ_EG_ACTION.NEXTVAL, 'PGRMastersComplaintGroupCreate', NULL, NULL, sysdate, '/masters/complaintGroupMaster!create.action', null, 1, (SELECT ID_MODULE from EG_MODULE where MODULE_NAME = 'PGRMasters'), 14, 'ComplaintGroupSave', 0, NULL,'pgr');
INSERT INTO EG_ACTION VALUES (SEQ_EG_ACTION.NEXTVAL, 'PGRMastersComplaintTypeNew', NULL, NULL, sysdate, '/masters/complaintTypeMaster!newform.action', null, 1, (SELECT ID_MODULE from EG_MODULE where MODULE_NAME = 'PGRMasters'), 15, 'Complaint Type', 1, NULL,'pgr');
INSERT INTO EG_ACTION VALUES (SEQ_EG_ACTION.NEXTVAL, 'PGRMastersComplaintTypeCreate', NULL, NULL, sysdate, '/masters/complaintTypeMaster.action', null, 1, (SELECT ID_MODULE from EG_MODULE where MODULE_NAME = 'PGRMasters'), 16, 'ComplaintTypeSave', 0, NULL,'pgr');
INSERT INTO EG_ACTION VALUES (SEQ_EG_ACTION.NEXTVAL, 'PGRMastersComplaintRoutineNew', NULL, NULL, sysdate, '/masters/complaintsRoutingMaster!newform.action', NULL, 1, (SELECT ID_MODULE from EG_MODULE where MODULE_NAME = 'PGRMasters'), 17, 'Complaint Routing', 1, NULL,'pgr'); 
INSERT INTO EG_ACTION VALUES (SEQ_EG_ACTION.NEXTVAL, 'PGRMastersComplaintRoutineCreate', NULL, NULL, sysdate, '/masters/complaintsRoutingMaster.action', null, 1, (SELECT ID_MODULE from EG_MODULE where MODULE_NAME = 'PGRMasters'), 18, 'ComplaintRoutingSave', 0, NULL,'pgr');

INSERT INTO EG_ACTION VALUES (SEQ_EG_ACTION.NEXTVAL, 'PGRCommonAajxComplaintTypeName', NULL, NULL, sysdate, '/common/commonPGRAjax!populateComplaintTypeName.action', NULL, 1, (SELECT ID_MODULE from EG_MODULE where MODULE_NAME = 'PGR'), 19, 'AjaxCompType', 0, NULL,'pgr');
INSERT INTO EG_ACTION VALUES (SEQ_EG_ACTION.NEXTVAL, 'PGRCommonAajxBoundary', NULL, NULL, sysdate, '/common//commonPGRAjax!populateBoundaryList.action', null, 1, (SELECT ID_MODULE from EG_MODULE where MODULE_NAME = 'PGR'), 20, 'AjaxBoundary', 0, NULL,'pgr');
INSERT INTO EG_ACTION VALUES (SEQ_EG_ACTION.NEXTVAL, 'PGRCommonAajxBoundaryCompType', NULL, NULL, sysdate, '/common/commonPGRAjax!populateBoundaryComplaintType.action', null, 1, (SELECT ID_MODULE from EG_MODULE where MODULE_NAME = 'PGR'), 21, 'AjaxBoundaryNCompType', 0, NULL,'pgr');
INSERT INTO EG_ACTION VALUES (SEQ_EG_ACTION.NEXTVAL, 'PGRCommonAajxDeleteCompRouteMap', NULL, NULL, sysdate, '/common/commonPGRAjax!deleteComplaintRouterMapping.action', null, 1, (SELECT ID_MODULE from EG_MODULE where MODULE_NAME = 'PGR'), 22, 'AjaxDelRouteMap', 0, NULL,'pgr');


INSERT INTO EG_ROLEACTION_MAP ( ROLEID, ACTIONID ) VALUES ((select ID_ROLE from eg_roles where ROLE_NAME = 'SuperUser'), (select ID from EG_ACTION where NAME = 'PGRIndex'));

INSERT INTO EG_ROLEACTION_MAP ( ROLEID, ACTIONID ) VALUES ((select ID_ROLE from eg_roles where ROLE_NAME = 'SuperUser'), (select ID from EG_ACTION where NAME = 'PGRComplaintRegister')); 
INSERT INTO EG_ROLEACTION_MAP ( ROLEID, ACTIONID ) VALUES ((select ID_ROLE from eg_roles where ROLE_NAME = 'SuperUser'), (select ID from EG_ACTION where NAME = 'PGRComplaintMyComplaints')); 
INSERT INTO EG_ROLEACTION_MAP ( ROLEID, ACTIONID ) VALUES ((select ID_ROLE from eg_roles where ROLE_NAME = 'SuperUser'), (select ID from EG_ACTION where NAME = 'PGRComplaintMyCompletedComplaints')); 

INSERT INTO EG_ROLEACTION_MAP ( ROLEID, ACTIONID ) VALUES ((select ID_ROLE from eg_roles where ROLE_NAME = 'SuperUser'), (select ID from EG_ACTION where NAME = 'PGRReportsByWard')); 
INSERT INTO EG_ROLEACTION_MAP ( ROLEID, ACTIONID ) VALUES ((select ID_ROLE from eg_roles where ROLE_NAME = 'SuperUser'), (select ID from EG_ACTION where NAME = 'PGRReportsByWardAggregate')); 
INSERT INTO EG_ROLEACTION_MAP ( ROLEID, ACTIONID ) VALUES ((select ID_ROLE from eg_roles where ROLE_NAME = 'SuperUser'), (select ID from EG_ACTION where NAME = 'PGRReportsByDepartmentwise')); 
INSERT INTO EG_ROLEACTION_MAP ( ROLEID, ACTIONID ) VALUES ((select ID_ROLE from eg_roles where ROLE_NAME = 'SuperUser'), (select ID from EG_ACTION where NAME = 'PGRReportsByDepartmentAggregate')); 
INSERT INTO EG_ROLEACTION_MAP ( ROLEID, ACTIONID ) VALUES ((select ID_ROLE from eg_roles where ROLE_NAME = 'SuperUser'), (select ID from EG_ACTION where NAME = 'PGRReportsByComplainerDetail')); 
INSERT INTO EG_ROLEACTION_MAP ( ROLEID, ACTIONID ) VALUES ((select ID_ROLE from eg_roles where ROLE_NAME = 'SuperUser'), (select ID from EG_ACTION where NAME = 'PGRReportsByComplaintGroup')); 
INSERT INTO EG_ROLEACTION_MAP ( ROLEID, ACTIONID ) VALUES ((select ID_ROLE from eg_roles where ROLE_NAME = 'SuperUser'), (select ID from EG_ACTION where NAME = 'PGRReportsByChart')); 
INSERT INTO EG_ROLEACTION_MAP ( ROLEID, ACTIONID ) VALUES ((select ID_ROLE from eg_roles where ROLE_NAME = 'SuperUser'), (select ID from EG_ACTION where NAME = 'PGRReportsByWardWiseKML')); 
INSERT INTO EG_ROLEACTION_MAP ( ROLEID, ACTIONID ) VALUES ((select ID_ROLE from eg_roles where ROLE_NAME = 'SuperUser'), (select ID from EG_ACTION where NAME = 'PGRReportsGeneric')); 

INSERT INTO EG_ROLEACTION_MAP ( ROLEID, ACTIONID ) VALUES ((select ID_ROLE from eg_roles where ROLE_NAME = 'SuperUser'), (select ID from EG_ACTION where NAME = 'PGRMastersComplaintGroupNew')); 
INSERT INTO EG_ROLEACTION_MAP ( ROLEID, ACTIONID ) VALUES ((select ID_ROLE from eg_roles where ROLE_NAME = 'SuperUser'), (select ID from EG_ACTION where NAME = 'PGRMastersComplaintGroupCreate')); 
INSERT INTO EG_ROLEACTION_MAP ( ROLEID, ACTIONID ) VALUES ((select ID_ROLE from eg_roles where ROLE_NAME = 'SuperUser'), (select ID from EG_ACTION where NAME = 'PGRMastersComplaintTypeNew')); 
INSERT INTO EG_ROLEACTION_MAP ( ROLEID, ACTIONID ) VALUES ((select ID_ROLE from eg_roles where ROLE_NAME = 'SuperUser'), (select ID from EG_ACTION where NAME = 'PGRMastersComplaintTypeCreate')); 
INSERT INTO EG_ROLEACTION_MAP ( ROLEID, ACTIONID ) VALUES ((select ID_ROLE from eg_roles where ROLE_NAME = 'SuperUser'), (select ID from EG_ACTION where NAME = 'PGRMastersComplaintRoutineNew')); 
INSERT INTO EG_ROLEACTION_MAP ( ROLEID, ACTIONID ) VALUES ((select ID_ROLE from eg_roles where ROLE_NAME = 'SuperUser'), (select ID from EG_ACTION where NAME = 'PGRMastersComplaintRoutineCreate')); 

INSERT INTO EG_ROLEACTION_MAP ( ROLEID, ACTIONID ) VALUES ((select ID_ROLE from eg_roles where ROLE_NAME = 'SuperUser'), (select ID from EG_ACTION where NAME = 'PGRCommonAajxComplaintTypeName')); 
INSERT INTO EG_ROLEACTION_MAP ( ROLEID, ACTIONID ) VALUES ((select ID_ROLE from eg_roles where ROLE_NAME = 'SuperUser'), (select ID from EG_ACTION where NAME = 'PGRCommonAajxBoundary')); 
INSERT INTO EG_ROLEACTION_MAP ( ROLEID, ACTIONID ) VALUES ((select ID_ROLE from eg_roles where ROLE_NAME = 'SuperUser'), (select ID from EG_ACTION where NAME = 'PGRCommonAajxBoundaryCompType')); 
INSERT INTO EG_ROLEACTION_MAP ( ROLEID, ACTIONID ) VALUES ((select ID_ROLE from eg_roles where ROLE_NAME = 'SuperUser'), (select ID from EG_ACTION where NAME = 'PGRCommonAajxDeleteCompRouteMap')); 

update eg_wf_types set wf_link='/pgr/complaint/complaint!view.action?model.id=:ID&mode=wf',full_qualified_name='org.egov.pgr.domain.entities.ComplaintDetails' where wf_type='ComplaintDetails';

---Config update DML -----

insert into eg_appconfig_values (id, key_id, effective_from, value)
values 
(seq_eg_appconfig_values.nextVal,(select id from eg_appconfig where key_name='CITY_LATITUDE' and module='egi'),sysdate, '21.09');

insert into eg_appconfig_values (id, key_id, effective_from, value)
values 
(seq_eg_appconfig_values.nextVal,(select id from eg_appconfig where key_name='CITY_LONGITUDE' and module='egi'),sysdate, '79.09');

INSERT INTO EG_APPCONFIG ( ID, KEY_NAME, DESCRIPTION, MODULE ) VALUES ( 
SEQ_EG_APPCONFIG.NEXTVAL, 'LOCATIONHTYPE', 'Location Heirarchy Type', 'PGR'); 

INSERT INTO EG_APPCONFIG_VALUES ( ID, KEY_ID, EFFECTIVE_FROM, VALUE ) VALUES ( 
SEQ_EG_APPCONFIG_VALUES.NEXTVAL, (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='LOCATIONHTYPE'),SYSDATE, 'LOCATION');

INSERT INTO EG_APPCONFIG ( ID, KEY_NAME, DESCRIPTION, MODULE ) VALUES ( 
SEQ_EG_APPCONFIG.NEXTVAL, 'GRIEVANCE_OFFICER_ROLE_NAME', 'GRIEVANCE OFFICER ROLE NAME', 'PGR'); 

INSERT INTO EG_APPCONFIG_VALUES ( ID, KEY_ID, EFFECTIVE_FROM, VALUE ) VALUES ( 
SEQ_EG_APPCONFIG_VALUES.NEXTVAL, (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='GRIEVANCE_OFFICER_ROLE_NAME'),SYSDATE, 'GRIEVANCE_OFFICER');

INSERT INTO EG_APPCONFIG ( ID, KEY_NAME, DESCRIPTION, MODULE ) VALUES ( 
SEQ_EG_APPCONFIG.NEXTVAL, 'HTYPENAME', 'Heirarchy Type Name', 'PGR'); 

INSERT INTO EG_APPCONFIG_VALUES ( ID, KEY_ID, EFFECTIVE_FROM, VALUE ) VALUES ( 
SEQ_EG_APPCONFIG_VALUES.NEXTVAL, (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='HTYPENAME'),SYSDATE, 'ADMINISTRATION');

INSERT INTO EG_APPCONFIG ( ID, KEY_NAME, DESCRIPTION, MODULE ) VALUES ( 
SEQ_EG_APPCONFIG.NEXTVAL, 'LOCAL_LANG_ENABLED', 'Is location language enabled', 'PGR'); 

INSERT INTO EG_APPCONFIG_VALUES ( ID, KEY_ID, EFFECTIVE_FROM, VALUE ) VALUES ( 
SEQ_EG_APPCONFIG_VALUES.NEXTVAL, (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='LOCAL_LANG_ENABLED'),SYSDATE, 'false');

INSERT INTO EG_APPCONFIG ( ID, KEY_NAME, DESCRIPTION, MODULE ) VALUES ( 
SEQ_EG_APPCONFIG.NEXTVAL, 'ULB_USER_COMPLAINT_STATUS_ACTION', 'Permissible Action By ULB User on Complaint Status ', 'PGR'); 

INSERT INTO EG_APPCONFIG ( ID, KEY_NAME, DESCRIPTION, MODULE ) VALUES ( 
SEQ_EG_APPCONFIG.NEXTVAL, 'CITIZEN_COMPLAINT_STATUS_ACTION', 'Permissible Action By Citizen on Complaint Status ', 'PGR'); 

INSERT INTO EG_APPCONFIG_VALUES ( ID, KEY_ID, EFFECTIVE_FROM, VALUE ) VALUES ( 
SEQ_EG_APPCONFIG_VALUES.NEXTVAL, (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='ULB_USER_COMPLAINT_STATUS_ACTION'),SYSDATE, 'REGISTERED');

INSERT INTO EG_APPCONFIG_VALUES ( ID, KEY_ID, EFFECTIVE_FROM, VALUE ) VALUES ( 
SEQ_EG_APPCONFIG_VALUES.NEXTVAL, (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='ULB_USER_COMPLAINT_STATUS_ACTION'),SYSDATE, 'FORWARDED');

INSERT INTO EG_APPCONFIG_VALUES ( ID, KEY_ID, EFFECTIVE_FROM, VALUE ) VALUES ( 
SEQ_EG_APPCONFIG_VALUES.NEXTVAL, (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='ULB_USER_COMPLAINT_STATUS_ACTION'),SYSDATE, 'PROCESSING');

INSERT INTO EG_APPCONFIG_VALUES ( ID, KEY_ID, EFFECTIVE_FROM, VALUE ) VALUES ( 
SEQ_EG_APPCONFIG_VALUES.NEXTVAL, (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='ULB_USER_COMPLAINT_STATUS_ACTION'),SYSDATE, 'COMPLETED');

INSERT INTO EG_APPCONFIG_VALUES ( ID, KEY_ID, EFFECTIVE_FROM, VALUE ) VALUES ( 
SEQ_EG_APPCONFIG_VALUES.NEXTVAL, (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='ULB_USER_COMPLAINT_STATUS_ACTION'),SYSDATE, 'REJECTED');

INSERT INTO EG_APPCONFIG_VALUES ( ID, KEY_ID, EFFECTIVE_FROM, VALUE ) VALUES ( 
SEQ_EG_APPCONFIG_VALUES.NEXTVAL, (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='CITIZEN_COMPLAINT_STATUS_ACTION'),SYSDATE, 'NOTCOMPLETED');

INSERT INTO EG_APPCONFIG_VALUES ( ID, KEY_ID, EFFECTIVE_FROM, VALUE ) VALUES ( 
SEQ_EG_APPCONFIG_VALUES.NEXTVAL, (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='CITIZEN_COMPLAINT_STATUS_ACTION'),SYSDATE, 'WITHDRAWN');

INSERT INTO EG_APPCONFIG_VALUES ( ID, KEY_ID, EFFECTIVE_FROM, VALUE ) VALUES ( 
SEQ_EG_APPCONFIG_VALUES.NEXTVAL, (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='CITIZEN_COMPLAINT_STATUS_ACTION'),SYSDATE, 'REOPENED');

update eg_router set positionid = (select POS_ID from EG_EIS_EMPLOYEEINFO ev where ev.USER_ID =userid
and ((ev.to_Date is null and ev.from_Date <= sysdate ) 
OR (ev.from_Date <= sysdate AND ev.to_Date >= sysdate)) 
and ev.IS_PRIMARY ='Y');

INSERT INTO EG_APPCONFIG ( ID, KEY_NAME, DESCRIPTION, MODULE ) VALUES ( 
SEQ_EG_APPCONFIG.NEXTVAL, 'SENDEMAILFORESCALATION', 'Send email for for escalation', 'PGR'); 

INSERT INTO EG_APPCONFIG_VALUES ( ID, KEY_ID, EFFECTIVE_FROM, VALUE ) VALUES ( 
SEQ_EG_APPCONFIG_VALUES.NEXTVAL, (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='SENDEMAILFORESCALATION'),SYSDATE, 'YES');


INSERT INTO eg_event_processor_spec(
            id, module, event_code, response_template)
    VALUES (eg_event_processor_spec_seq.nextval, 'PGR', 'Complaint Escalation', '<response><email><to>${to_address}</to><subject>${subject}</subject><body>Dear ${to_name} , Escalated</body></email></response>');

#DOWN
---Action update down script---

DELETE FROM EG_ROLEACTION_MAP WHERE ACTIONID IN (SELECT ID FROM EG_ACTION WHERE CONTEXT_ROOT='pgr');
DELETE FROM EG_ACTION WHERE CONTEXT_ROOT='pgr';
DELETE FROM EG_MODULE WHERE BASEURL='pgr';

---Config update down script---
delete from eg_appconfig_values where key_id = (select id from eg_appconfig where key_name='CITY_LATITUDE' and module='egi')
and value='21.09';

delete from eg_appconfig_values where key_id = (select id from eg_appconfig where key_name='CITY_LONGITUDE' and module='egi')
and value='79.09';

delete from eg_appconfig where key_name='CITY_LATITUDE' and module='egi';
delete from eg_appconfig where key_name='CITY_LONGITUDE'  and module='egi';


DELETE FROM EG_APPCONFIG_VALUES WHERE KEY_ID IN (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='GRIEVANCE_OFFICER_ROLE_NAME');
delete from EG_APPCONFIG where MODULE='PGR' and KEY_NAME = 'GRIEVANCE_OFFICER_ROLE_NAME';

DELETE FROM EG_APPCONFIG_VALUES WHERE KEY_ID IN (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='LOCATIONHTYPE');
delete from EG_APPCONFIG where MODULE='PGR' and KEY_NAME = 'LOCATIONHTYPE';

DELETE FROM EG_APPCONFIG_VALUES WHERE KEY_ID IN (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='HTYPENAME');
delete from EG_APPCONFIG where MODULE='PGR' and KEY_NAME = 'HTYPENAME';
DELETE FROM EG_APPCONFIG_VALUES WHERE KEY_ID IN (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='LOCAL_LANG_ENABLED');
delete from EG_APPCONFIG where MODULE='PGR' and KEY_NAME = 'LOCAL_LANG_ENABLED';

delete from EG_APPCONFIG_VALUES where KEY_ID = (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='CITIZEN_COMPLAINT_STATUS_ACTION') and VALUE='REOPENED';
delete from EG_APPCONFIG_VALUES where KEY_ID = (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='CITIZEN_COMPLAINT_STATUS_ACTION') and VALUE='WITHDRAWN';
delete from EG_APPCONFIG_VALUES where KEY_ID = (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='CITIZEN_COMPLAINT_STATUS_ACTION') and VALUE='NOTCOMPLETED';
delete from EG_APPCONFIG_VALUES where KEY_ID = (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='ULB_USER_COMPLAINT_STATUS_ACTION') and VALUE='REJECTED';
delete from EG_APPCONFIG_VALUES where KEY_ID = (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='ULB_USER_COMPLAINT_STATUS_ACTION') and VALUE='COMPLETED';
delete from EG_APPCONFIG_VALUES where KEY_ID = (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='ULB_USER_COMPLAINT_STATUS_ACTION') and VALUE='PROCESSING';
delete from EG_APPCONFIG_VALUES where KEY_ID = (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='ULB_USER_COMPLAINT_STATUS_ACTION') and VALUE='FORWARDED';
delete from EG_APPCONFIG_VALUES where KEY_ID = (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='ULB_USER_COMPLAINT_STATUS_ACTION') and VALUE='REGISTERED';

delete from EG_APPCONFIG where key_name='CITIZEN_COMPLAINT_STATUS_ACTION';
delete from EG_APPCONFIG where key_name='ULB_USER_COMPLAINT_STATUS_ACTION';
update eg_router set postitionid = null;
DELETE FROM EG_APPCONFIG_VALUES WHERE KEY_ID = (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='SENDEMAILFORESCALATION');
delete from EG_APPCONFIG where MODULE='PGR' and KEY_NAME = 'SENDEMAILFORESCALATION';
delete from eg_event_processor_spec where module='PGR' and event_code='Complaint Escalation';

