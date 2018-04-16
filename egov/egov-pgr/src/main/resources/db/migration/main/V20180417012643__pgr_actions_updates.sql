update eg_module set contextroot='pgr' where parentmodule=(select id from eg_module where name='Pgr Reports');
update eg_module set contextroot='pgr' where parentmodule=(select id from eg_module where name='Pgr Masters');
update eg_module set contextroot='pgr' where parentmodule=(select id from eg_module where name='PGR');
update eg_module set name='PGR_MASTERS', ordernumber=2 where name='Pgr Masters';
update eg_module set name='PGR_REPORTS', ordernumber=3 where name='Pgr Reports';
update eg_module set name='PGR_MAIN', ordernumber=1 where name='PGRComplaints';
update eg_module set name='PGR_GENERIC', ordernumber=0 where name='PGR-COMMON';
update eg_module set name='PGR_MASTERS_ESCALATION', ordernumber=3 where name='Escalation';
update eg_module set name='PGR_MASTERS_ROUTER', ordernumber=2 where name='Router';
update eg_module set name='PGR_MASTERS_CATEGORY', ordernumber=0 where name='Grievance Category';
update eg_module set name='PGR_MASTERS_GRIEVANCETYPE', ordernumber=1 where name='Complaint Type';
update eg_action set parentmodule=(select id from eg_module where name='PGR_MASTERS_ESCALATION') where parentmodule=(select id from eg_module where name='Escalation Time');

delete from eg_module where name='Escalation Time';
delete from eg_roleaction where actionid in (select id from eg_action where parentmodule=(select id from eg_module where name='Feedback'));
delete from eg_feature_action where action in (select id from eg_action where parentmodule=(select id from eg_module where name='Feedback'));
delete from eg_action where parentmodule=(select id from eg_module where name='Feedback');
delete from eg_module where name='Feedback';
update eg_action set parentmodule=(select id from eg_module where name='PGR_REPORTS') where parentmodule in (select id from eg_module where name in ('Ageing Report','Drill Down Report','Complaint Type Wise Report','Functionary Wise Report','Router Escalation Report'));
delete from eg_module where name in ('Ageing Report','Drill Down Report','Complaint Type Wise Report','Functionary Wise Report','Router Escalation Report');
delete from eg_action where parentmodule in (select id from eg_module where name in ('Index','Find Reports','Find Status Reports','Find Aggregated Status Reports','View PieCharts','Register Complaint'));
delete from eg_module where name in ('Index','Find Reports','Find Status Reports','Find Aggregated Status Reports','View PieCharts','Register Complaint');
update eg_module set rootmodule=(select id from eg_module where name='PGR') where contextroot='pgr' and parentmodule is not null;
update eg_action set ordernumber=0 where parentmodule in (select id from eg_module where rootmodule=(select id from eg_module where name='PGR')) and enabled=false and ordernumber is null;

update eg_action set name='GRIEVANCE_SEARCH_OFFICIAL',ordernumber=2 where name='SearchComplaintFormOfficial';
update eg_action set name='GRIEVANCE_MY_PENDING',displayname='My Pending Grievances', ordernumber=1 where name='PendingGrievance';
update eg_action set name='GRIEVANCE_REGISTER_OFFICIAL',displayname='Register Grievance',ordernumber=3 where name='ComplaintRegisterationOfficials';
update eg_action set name='GRIEVANCE_REGISTER_CITIZEN',ordernumber=4,enabled=false where name='ComplaintRegisteration';
update eg_action set name='GRIEVANCE_REGISTER_CITIZEN_TYPE_DROPDOWN' where name='ComplaintTypeAjax';
update eg_action set name='GRIEVANCE_REGISTER_OFFICIAL_TYPE_DROPDOWN' where name='ComplaintTypeAjaxOfficials';
update eg_action set name='GRIEVANCE_REGISTER_CITIZEN_TYPE_BY_CATEGORY_DROPDOWN' where name='ListComplaintTypeByCategory';
update eg_action set name='GRIEVANCE_REGISTER_OFFICIAL_TYPE_BY_CATEGORY_DROPDOWN' where name='ListComplaintTypeByCategoryOfficials';
update eg_action set name='GRIEVANCE_REGISTER_OFFICIAL_LOCATION_DROPDOWN' where name='ComplaintLocationsOfficials';
update eg_action set name='GRIEVANCE_REGISTER_CRN_REQUIRED_CHECK' where name='RCCRNRequiredOfficials';
update eg_action set name='GRIEVANCE_REGISTER_LOCATION_REQUIRED_CHECK' where name='ComplaintLocationRequired';
update eg_action set name='GRIEVANCE_REGISTER_OFFICIAL_LOCATION_REQUIRED_CHECK' where name='ComplaintLocationRequiredOfficials';
update eg_action set name='GRIEVANCE_SEARCH_ANONYMOUS' where name='SearchComplaintForm';
update eg_action set name='GRIEVANCE_VIEW_IMAGE_DOWNLOAD' where name='complaint downloadfile';
update eg_action set name='GRIEVANCE_REGISTER_SAVE' where name='ComplaintSave';
update eg_action set name='GRIEVANCE_REGISTER_OFFICIAL_SAVE' where name='ComplaintSaveOfficials';
update eg_action set name='GRIEVANCE_UPDATE_CITIZEN' where name='Citizen Update Complaint';
update eg_action set name='GRIEVANCE_MY_PENDING_SEARCH' where name='AjaxPendingGrievanceResult';
update eg_action set name='GRIEVANCE_VIEW' where name='View Complaint';
update eg_action set name='GRIEVANCE_UPDATE_WF_POSITION_DROPDOWN' where name='load Positions';
update eg_action set name='GRIEVANCE_UPDATE_WF_DESIGNATION_DROPDOWN' where name='load Designations';
update eg_action set name='GRIEVANCE_UPDATE_LOCATION_DROPDOWN' where name='loadchildlocations';
update eg_action set name='GRIEVANCE_REGISTER_CITIZEN_LOCATION_DROPDOWN' where name='ComplaintLocations';

update eg_action set name='ROUTER_MODIFY',displayname='Modify Router',ordernumber=3 where name='Search Complaint Router';
update eg_action set name='ROUTER_DEFINE_SINGLE',displayname='Define Router',ordernumber=1 where name='Create Complaint Router';
update eg_action set name='ROUTER_DEFINE_BULK',displayname='Define Bulk Router',ordernumber=2 where name='Search Bulk Router Generation';
update eg_action set name='ROUTER_VIEW',ordernumber=4 where name='View Complaint Router';
update eg_action set name='ROUTER_SEARCH_FOR_VIEW' where name='View Complaint RouterViaSearch';
update eg_action set name='ROUTER_DELETE' where name='Delete Complaint Router';
update eg_action set name='ROUTER_RECORD_DOWNLOAD' where name='ComplaintRouter Download';
update eg_action set name='ROUTER_UPDATE' where name='Update Complaint Router';
update eg_action set name='ROUTER_SEARCH' where name='Search viewRouter Result';
update eg_action set name='ROUTER_GRIEVANCETYPE_DROPDOWN' where name='AjaxRouterComplaintType';
update eg_action set name='ROUTER_POSITION_DROPDOWN' where name='AjaxRouterPosition';
update eg_action set name='ROUTER_BOUNDARY_DROPDOWN' where name='AjaxRouterBoundariesbyType';
update eg_action set name='ROUTER_DIFINE_BULK_SAVE' where name='Save Bulk Router';

update eg_action set name='GRIEVANCE_TYPE_VIEW', ordernumber=3 where name='ViewComplaintType';
update eg_action set name='GRIEVANCE_TYPE_MODIFY', displayname='Modify Grievance Type', ordernumber=2 where name='Search ComplaintType';
update eg_action set name='GRIEVANCE_TYPE_CREATE', ordernumber=1 where name='Create ComplaintType';
update eg_action set name='GRIEVANCE_TYPE_SEARCH_BY_DEPARTMENT' where name='Complaint Type Search By Department';
update eg_action set name='GRIEVANCE_TYPE_MODIFY_SAVE' where name='Update ComplaintType';

update eg_action set name='GRIEVANCE_CATEGORY_MODIFY', displayname='Modify Grievance Category', ordernumber=2 where name='Search For Edit Grievance Category';
update eg_action set name='GRIEVANCE_CATEGORY_CREATE', displayname='Create Grievance Category', ordernumber=1 where name='Create Grievance Category';
update eg_action set name='GRIEVANCE_CATEGORY_MODIFY_SAVE' where name='Update Grievance Category';

update eg_action set name='ESCALATION_HIERARCHY_DEFINE_BULK',displayname='Define Bulk Escalation Hierarchy',ordernumber=2 where name='Search Bulk Escalation';
update eg_action set name='ESCALATION_HIERARCHY_DEFINE_SINGLE',displayname='Define Escalation Hierarchy',ordernumber=1 where name='Define Escalation';
update eg_action set name='ESCALATION_TIME_DEFINE',ordernumber=3 where name='Define Escalation Time';
update eg_action set name='ESCALATION_HIERARCHY_VIEW',ordernumber=4 where name='View Escalation';
update eg_action set name='ESCALATION_TIME_VIEW_ESCALATION_TIME',ordernumber=5 where name='View Escalation Time';
update eg_action set name='ESCALATION_TIME_DEFINE_SAVE' where name='Save Escalation Time';
update eg_action set name='ESCALATION_TIME_GRIEVANCE_TYPE_DROPDOWN' where name='AjaxEscTimeComplaintType';
update eg_action set name='ESCALATION_TIME_DESIGNATION_DROPDOWN' where name='AjaxEscTimeDesignation';
update eg_action set name='ESCALATION_HIERARCHY_DESIGNATION_BY_DEPT_DROPDOWN' where name='load Designations for escalation';
update eg_action set name='ESCALATION_HIERARCHY_SINGLE_UPDATE' where name='Update Escalation';
update eg_action set name='ESCALATION_HIERARCHY_BULK_UPDATE' where name='Save Bulk Escalation';
update eg_action set name='ESCALATION_HIERARCHY_POSITION_DROPDOWN' where name='AjaxEscalationPosition';
update eg_action set name='ESCALATION_HIERARCHY_BOUNDARY_DROPDOWN' where name='AjaxEscalationBndryType';
update eg_action set name='ESCALATION_HIERARCHY_POSITION_DROPDOWN_BY_DEPT_DESIG' where name='load position for escalation';

update eg_action set name='REPORT_BOUNDARYWISE_DRILLDOWN',ordernumber=1,displayname='Boundarywise Drilldown Report' where name='Boundarywise Drilldown Report';
update eg_action set name='REPORT_BOUNDARYWISE_AGEING',ordernumber=2,displayname='Boundarywise Ageing Report' where name='Boundarywise Ageing Report';
update eg_action set name='REPORT_DEPARTMENTWISE_DRILLDOWN',ordernumber=3,displayname='Departmentwise Drilldown Report' where name='Departmentwise Drilldown Report';
update eg_action set name='REPORT_DEPARTMENTWISE_AGEING',ordernumber=4,displayname='Departmentwise Ageing Report' where name='Departmentwise Ageing Report';
update eg_action set name='REPORT_GRIEVANCETYPEWISE_AGEING',ordernumber=5,displayname='Grievance Typewise Report' where name='GrievanceTypewise Report Search';
update eg_action set name='REPORT_FUNCTIONARYWISE_AGEING',ordernumber=6,displayname='Functionarywise Report' where name='Functionarywise Report Search';
update eg_action set name='REPORT_ESCALATION_ROUTER',ordernumber=7,displayname='Escalation Router Report' where name='EscalationRouter Report Search';

update eg_action set name='REPORT_FUNCTIONARYWISE_AGEING_DOWNLOAD' where name='Download Functionarywise Report';
update eg_action set name='REPORT_DRILLDOWN_SEARCH' where name='Drilldown Report Search';
update eg_action set name='REPORT_GRIEVANCE_TYPE_DROPDOWN' where name='AjaxCallInReportForcomplaintType';
update eg_action set name='REPORT_GRIEVANCE_TYPEWISE_TYPE_DROPDOWN' where name='ajaxsearchallcomplaintTypes';
update eg_action set name='REPORT_ESCALATION_ROUTER_DOWNLOAD' where name='Download EscalationRouter Report';
update eg_action set name='REPORT_AGEING_GRAND_TOTAL' where name='Ageing report Grand Total';
update eg_action set name='REPORT_AGEING_DOWNLOAD' where name='Ageing report Download';
update eg_action set name='REPORT_AGEING_SEARCH' where name='AgeingReport Search';
update eg_action set name='REPORT_GRIEVANCE_TYPEWISE_DOWNLOAD' where name='Download GrievanceTypewise Report';
update eg_action set name='REPORT_GRIEVANCE_TYPEWISE_GRAND_TOTAL' where name='GrievanceTypewise Report Grandtotal';
update eg_action set name='REPORT_FUNCTIONARYWISE_GRAND_TOTAL' where name='Functionarywise Report Grandtotal';
update eg_action set name='REPORT_DRILLDOWN_GRAND_TOTAL' where name='Drill Down report Grand Total';
update eg_action set name='REPORT_DRILLDOWN_DOWNLOAD' where name='Drill Down report Download';

update eg_module set name='PGR_FEEDBACK',ordernumber=4,displayname='Feedback' where name='IVRS';
update eg_action set name='FEEDBACK_CREATE_REASON' where name='Complaint Feedback Reason';
update eg_action set name='FEEDBACK_VIEW_REVIEW' where name='Complaint Feedback Review';
update eg_action set name='FEEDBACK_SEARCH_FOR_REVIEW' where name='Search Rated Grievance';

update eg_action set parentmodule=(select id from eg_module where name='PGR_MASTERS_ROUTER') where contextroot='pgr' and name like 'ROUTER_%';
update eg_action set parentmodule=(select id from eg_module where name='PGR_MAIN') where name in ('GRIEVANCE_REGISTER_CITIZEN_TYPE_BY_CATEGORY_DROPDOWN', 'GRIEVANCE_REGISTER_OFFICIAL_TYPE_BY_CATEGORY_DROPDOWN','GRIEVANCE_UPDATE_CITIZEN','GRIEVANCE_UPDATE_LOCATION_DROPDOWN','GRIEVANCE_UPDATE_WF_DESIGNATION_DROPDOWN','GRIEVANCE_UPDATE_WF_POSITION_DROPDOWN');

