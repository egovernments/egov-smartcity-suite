


update eg_action set url='/router/search/update' where url='/router/update-search';

--------------------------------------------------------Start eg_feature-----------------------------------------------------------------

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Grievance Type','Create a Grievance Type',
(select id from eg_module  where name = 'PGR'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Modify Grievance Type','Modify an existing Grievance Type',(select id from eg_module  where name = 'PGR'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Grievance Type','View an existing Grievance Type',(select id from eg_module  where name = 'PGR'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Bulk Router','Create Routing in bulk',
(select id from eg_module  where name = 'PGR'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Router','Create a Routing',
(select id from eg_module  where name = 'PGR'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Router','View an existing Router',
(select id from eg_module  where name = 'PGR'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Modify Router','Modify an existing Router',
(select id from eg_module  where name = 'PGR'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Define Escalation Time','Define Grievance Escalation Time',
(select id from eg_module  where name = 'PGR'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Search Escalation Time','Search Grievance Escalation Time',
(select id from eg_module  where name = 'PGR'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Category','Create a Grievance Category',
(select id from eg_module  where name = 'PGR'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Modify Category','Modify an existing Grievance Category',(select id from eg_module  where name = 'PGR'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Bulk Escalation','Create Escalation in bulk',
(select id from eg_module  where name = 'PGR'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Define Escalation','Define a Grievance Escalation',
(select id from eg_module  where name = 'PGR'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Escalation','View an existing Grievance Escalation',
(select id from eg_module  where name = 'PGR'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Search Grievance','Search existing Grievances',
(select id from eg_module  where name = 'PGR'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'My Pending Grievance','View all Assigned Grievance for an Official',
(select id from eg_module  where name = 'PGR'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Citizen Grievance Registration','Citizen Grievance Registration',
(select id from eg_module  where name = 'PGR'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Officials Grievance Registration','Officials Grievance Registration',(select id from eg_module  where name = 'PGR'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Departmentwise Ageing Report','Ageing Report By Departmentwise',(select id from eg_module  where name = 'PGR'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Boundarywise Ageing Report','Ageing Report By Boundarywise',(select id from eg_module  where name = 'PGR'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Departmentwise Drill Down Report','Drill Down Report By Departmentwise',(select id from eg_module  where name = 'PGR'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Boundarywise Drill Down Report','Drill Down Report By Boundarywise',(select id from eg_module  where name = 'PGR'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Grievance Typewise Report','Grievance Typewise Report',(select id from eg_module  where name = 'PGR'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Functionarywise Report','Functionarywise Report',(select id from eg_module  where name = 'PGR'));

--------------------------------------------------------End eg_feature---------------------------------------------------------------------



--------------------------------------------------------Start eg_feature_action-----------------------------------------------------------

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Add Complaint Type') ,(select id FROM eg_feature WHERE name = 'Create Grievance Type'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'complaintTypeSuccess') ,(select id FROM eg_feature WHERE name = 'Create Grievance Type'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'UpdateComplaintType') ,(select id FROM eg_feature WHERE name = 'Create Grievance Type'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'UpdateComplaintType') ,(select id FROM eg_feature WHERE name = 'Modify Grievance Type'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Citizen Update Complaint') ,(select id FROM eg_feature WHERE name = 'Modify Grievance Type'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'ViewComplaintType') ,(select id FROM eg_feature WHERE name = 'View Grievance Type'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'View Complaint') ,(select id FROM eg_feature WHERE name = 'View Grievance Type'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'viewComplaintTypeResult') ,(select id FROM eg_feature WHERE name = 'View Grievance Type'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Search Bulk Router Generation') ,(select id FROM eg_feature WHERE name = 'Create Bulk Router'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'ListComplaintTypeByCategoryOfficials') ,(select id FROM eg_feature WHERE name = 'Create Bulk Router'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxLoadBoundarys') ,(select id FROM eg_feature WHERE name = 'Create Bulk Router'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxRouterPosition') ,(select id FROM eg_feature WHERE name = 'Create Bulk Router'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Save Bulk Router') ,(select id FROM eg_feature WHERE name = 'Create Bulk Router'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Create Router') ,(select id FROM eg_feature WHERE name = 'Create Router'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxRouterComplaintType') ,(select id FROM eg_feature WHERE name = 'Create Router'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxRouterBoundariesbyType') ,(select id FROM eg_feature WHERE name = 'Create Router'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxRouterPosition') ,(select id FROM eg_feature WHERE name = 'Create Router'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Update Router') ,(select id FROM eg_feature WHERE name = 'Create Router'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Delete Router') ,(select id FROM eg_feature WHERE name = 'Create Router'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'View Router') ,(select id FROM eg_feature WHERE name = 'View Router'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'ajaxsearchallcomplaintTypes') ,(select id FROM eg_feature WHERE name = 'View Router'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxRouterBoundariesbyType') ,(select id FROM eg_feature WHERE name = 'View Router'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Search viewRouter Result') ,(select id FROM eg_feature WHERE name = 'View Router'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'RouterView') ,(select id FROM eg_feature WHERE name = 'View Router'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Edit Router') ,(select id FROM eg_feature WHERE name = 'Modify Router'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxRouterBoundariesbyType') ,(select id FROM eg_feature WHERE name = 'Modify Router'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'update RouterViaSearch') ,(select id FROM eg_feature WHERE name = 'Modify Router'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'ajaxsearchallcomplaintTypes') ,(select id FROM eg_feature WHERE name = 'Modify Router'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Search updateRouter Result') ,(select id FROM eg_feature WHERE name = 'Modify Router'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Search viewRouter Result') ,(select id FROM eg_feature WHERE name = 'Modify Router'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Define Escalation Time') ,(select id FROM eg_feature WHERE name = 'Define Escalation Time'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxEscTimeComplaintType') ,(select id FROM eg_feature WHERE name = 'Define Escalation Time'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxEscTimeDesignation') ,(select id FROM eg_feature WHERE name = 'Define Escalation Time'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Save Escalation Time') ,(select id FROM eg_feature WHERE name = 'Define Escalation Time'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Search Escalation Time') ,(select id FROM eg_feature WHERE name = 'Search Escalation Time'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Search Escalation Time result') ,(select id FROM eg_feature WHERE name = 'Search Escalation Time'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'ajaxsearchallcomplaintTypes') ,(select id FROM eg_feature WHERE name = 'Search Escalation Time'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES
((select id FROM eg_action  WHERE name = 'load Designations') ,(select id FROM eg_feature WHERE name = 'Search Escalation Time'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxEscTimeDesignation') ,(select id FROM eg_feature WHERE name = 'Search Escalation Time'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Create Grievance Category') ,(select id FROM eg_feature WHERE name = 'Create Category'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Search For Edit Grievance Category') ,(select id FROM eg_feature WHERE name = 'Modify Category'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Update Grievance Category') ,(select id FROM eg_feature WHERE name = 'Modify Category'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Search Bulk Escalation') ,(select id FROM eg_feature WHERE name = 'Create Bulk Escalation'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxEscalationPosition') ,(select id FROM eg_feature WHERE name = 'Create Bulk Escalation'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Save Bulk Escalation') ,(select id FROM eg_feature WHERE name = 'Create Bulk Escalation'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'ListComplaintTypeByCategoryOfficials') ,(select id FROM eg_feature WHERE name = 'Create Bulk Escalation'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Define Escalation') ,(select id FROM eg_feature WHERE name = 'Define Escalation'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxEscalationPosition') ,(select id FROM eg_feature WHERE name = 'Define Escalation'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'load Designations for escalation') ,(select id FROM eg_feature WHERE name = 'Define Escalation'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'load position for escalation') ,(select id FROM eg_feature WHERE name = 'Define Escalation'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Update Escalation') ,(select id FROM eg_feature WHERE name = 'Define Escalation'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'View Escalation') ,(select id FROM eg_feature WHERE name = 'View Escalation'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'ajaxsearchallcomplaintTypes') ,(select id FROM eg_feature WHERE name = 'View Escalation'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxEscalationPosition') ,(select id FROM eg_feature WHERE name = 'View Escalation'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Search Escalation result') ,(select id FROM eg_feature WHERE name = 'View Escalation'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxEscalationBndryType') ,(select id FROM eg_feature WHERE name = 'View Escalation'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'SearchComplaintFormOfficial') ,(select id FROM eg_feature WHERE name = 'Search Grievance'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'SearchComplaintForm') ,(select id FROM eg_feature WHERE name = 'Search Grievance'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'ajaxsearchallcomplaintTypes') ,(select id FROM eg_feature WHERE name = 'Search Grievance'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'ComplaintTypeAjax') ,(select id FROM eg_feature WHERE name = 'Search Grievance'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'ComplaintLocationRequired') ,(select id FROM eg_feature WHERE name = 'Search Grievance'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'PendingGrievance') ,(select id FROM eg_feature WHERE name = 'My Pending Grievance'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxPendingGrievanceResult') ,(select id FROM eg_feature WHERE name = 'My Pending Grievance'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'load Designations') ,(select id FROM eg_feature WHERE name = 'My Pending Grievance'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'load Positions') ,(select id FROM eg_feature WHERE name = 'My Pending Grievance'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Citizen Update Complaint') ,(select id FROM eg_feature WHERE name = 'My Pending Grievance'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'ComplaintRegisteration') ,(select id FROM eg_feature WHERE name = 'Citizen Grievance Registration'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'ComplaintSave') ,(select id FROM eg_feature WHERE name = 'Citizen Grievance Registration'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'load Designations') ,(select id FROM eg_feature WHERE name = 'Citizen Grievance Registration'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'ListComplaintTypeByCategory') ,(select id FROM eg_feature WHERE name = 'Citizen Grievance Registration'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'ComplaintLocations') ,(select id FROM eg_feature WHERE name = 'Citizen Grievance Registration'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'load Positions') ,(select id FROM eg_feature WHERE name = 'Citizen Grievance Registration'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'loadchildlocations') ,(select id FROM eg_feature WHERE name = 'Citizen Grievance Registration'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'complaint downloadfile') ,(select id FROM eg_feature WHERE name = 'Citizen Grievance Registration'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'ComplaintRegisterationOfficials') ,(select id FROM eg_feature WHERE name = 'Officials Grievance Registration'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'ComplaintTypeAjaxOfficials') ,(select id FROM eg_feature WHERE name = 'Officials Grievance Registration'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'RCCRNRequiredOfficials') ,(select id FROM eg_feature WHERE name = 'Officials Grievance Registration'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'load Designations') ,(select id FROM eg_feature WHERE name = 'Officials Grievance Registration'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'load Positions') ,(select id FROM eg_feature WHERE name = 'Officials Grievance Registration'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'ComplaintLocationRequiredOfficials') ,(select id FROM eg_feature WHERE name = 'Officials Grievance Registration'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'ListComplaintTypeByCategory') ,(select id FROM eg_feature WHERE name = 'Officials Grievance Registration'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'ComplaintSaveOfficials') ,(select id FROM eg_feature WHERE name = 'Officials Grievance Registration'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'ListComplaintTypeByCategoryOfficials') ,(select id FROM eg_feature WHERE name = 'Officials Grievance Registration'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'ComplaintLocationsOfficials') ,(select id FROM eg_feature WHERE name = 'Officials Grievance Registration'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Ageing Report By Department wise') ,(select id FROM eg_feature WHERE name = 'Departmentwise Ageing Report'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Ageing report search result') ,(select id FROM eg_feature WHERE name = 'Departmentwise Ageing Report'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Ageing Report By Boundary wise') ,(select id FROM eg_feature WHERE name = 'Boundarywise Ageing Report'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Ageing report search result') ,(select id FROM eg_feature WHERE name = 'Boundarywise Ageing Report'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES
((select id FROM eg_action  WHERE name = 'Drill Down Report By Department wise') ,(select id FROM eg_feature WHERE name = 'Departmentwise Drill Down Report'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES
((select id FROM eg_action  WHERE name = 'Drill Down Report search result') ,(select id FROM eg_feature WHERE name = 'Departmentwise Drill Down Report'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Drill Down Report By Boundary wise') ,(select id FROM eg_feature WHERE name = 'Boundarywise Drill Down Report'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Drill Down Report search result') ,(select id FROM eg_feature WHERE name = 'Boundarywise Drill Down Report'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Complaint Type Wise Report search result') ,(select id FROM eg_feature WHERE name = 'Grievance Typewise Report'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Complaint Type Wise Report') ,(select id FROM eg_feature WHERE name = 'Grievance Typewise Report'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Functionary Wise Report Search') ,(select id FROM eg_feature WHERE name = 'Functionarywise Report'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Functionary Wise Report Result') ,(select id FROM eg_feature WHERE name = 'Functionarywise Report'));

-----------------------------------------------------End eg_feature_action-------------------------------------------------------------

----------------------------------------------------Start eg_feature_role------------------------------------------------------------------

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='Super User') ,(select id FROM eg_feature WHERE name = 'Create Grievance Type'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='Grivance Administrator') ,(select id FROM eg_feature WHERE name = 'Create Grievance Type'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='Super User') ,(select id FROM eg_feature WHERE name = 'Modify Grievance Type'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='Grivance Administrator') ,(select id FROM eg_feature WHERE name = 'Modify Grievance Type'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='Super User') ,(select id FROM eg_feature WHERE name = 'View Grievance Type'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='Grivance Administrator') ,(select id FROM eg_feature WHERE name = 'View Grievance Type'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='Super User'),(select id FROM eg_feature WHERE name = 'Create Bulk Router'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='Grivance Administrator'),(select id FROM eg_feature WHERE name = 'Create Bulk Router'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='Super User') ,(select id FROM eg_feature WHERE name = 'Create Router'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='Grivance Administrator') ,(select id FROM eg_feature WHERE name = 'Create Router'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='Super User') ,(select id FROM eg_feature WHERE name = 'Modify Router'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='Grivance Administrator') ,(select id FROM eg_feature WHERE name = 'Modify Router'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='Super User') ,(select id FROM eg_feature WHERE name = 'View Router'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='Grivance Administrator') ,(select id FROM eg_feature WHERE name = 'View Router'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='Super User') ,(select id FROM eg_feature WHERE name = 'Define Escalation Time'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='Grivance Administrator') ,(select id FROM eg_feature WHERE name = 'Define Escalation Time'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='Super User') ,(select id FROM eg_feature WHERE name = 'Search Escalation Time'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='Grivance Administrator') ,(select id FROM eg_feature WHERE name = 'Search Escalation Time'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='Super User') ,(select id FROM eg_feature WHERE name = 'Create Category'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='Grivance Administrator') ,(select id FROM eg_feature WHERE name = 'Create Category'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='Super User') ,(select id FROM eg_feature WHERE name = 'Modify Category'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='Grivance Administrator') ,(select id FROM eg_feature WHERE name = 'Modify Category'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='Super User') ,(select id FROM eg_feature WHERE name = 'Create Bulk Escalation'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='Grivance Administrator') ,(select id FROM eg_feature WHERE name = 'Create Bulk Escalation'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='Super User') ,(select id FROM eg_feature WHERE name = 'Define Escalation'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='Grivance Administrator') ,(select id FROM eg_feature WHERE name = 'Define Escalation'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='Super User') ,(select id FROM eg_feature WHERE name = 'View Escalation'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='Grivance Administrator') ,(select id FROM eg_feature WHERE name = 'View Escalation'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='Super User') ,(select id FROM eg_feature WHERE name = 'Search Grievance'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='Grivance Administrator') ,(select id FROM eg_feature WHERE name = 'Search Grievance'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='Grievance Officer') ,(select id FROM eg_feature WHERE name = 'Search Grievance'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='Redressal Officer') ,(select id FROM eg_feature WHERE name = 'Search Grievance'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='ERP Report Viewer') ,(select id FROM eg_feature WHERE name = 'Search Grievance'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='Super User') ,(select id FROM eg_feature WHERE name = 'My Pending Grievance'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='Citizen') ,(select id FROM eg_feature WHERE name = 'Citizen Grievance Registration'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='Grievance Officer') ,(select id FROM eg_feature WHERE name = 'Officials Grievance Registration'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='Redressal Officer') ,(select id FROM eg_feature WHERE name = 'Officials Grievance Registration'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='Super User') ,(select id FROM eg_feature WHERE name = 'Officials Grievance Registration'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='Super User') ,(select id FROM eg_feature WHERE name = 'Departmentwise Ageing Report'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='Redressal Officer') ,(select id FROM eg_feature WHERE name = 'Departmentwise Ageing Report'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='Grievance Officer') ,(select id FROM eg_feature WHERE name = 'Departmentwise Ageing Report'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='ERP Report Viewer') ,(select id FROM eg_feature WHERE name = 'Departmentwise Ageing Report'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='ERP Report Viewer') ,(select id FROM eg_feature WHERE name = 'Boundarywise Ageing Report'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='Grievance Officer') ,(select id FROM eg_feature WHERE name = 'Boundarywise Ageing Report'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='Redressal Officer') ,(select id FROM eg_feature WHERE name = 'Boundarywise Ageing Report'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='Super User') ,(select id FROM eg_feature WHERE name = 'Boundarywise Ageing Report'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='Super User') ,(select id FROM eg_feature WHERE name = 'Departmentwise Drill Down Report'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='Redressal Officer') ,(select id FROM eg_feature WHERE name = 'Departmentwise Drill Down Report'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='Grievance Officer') ,(select id FROM eg_feature WHERE name = 'Departmentwise Drill Down Report'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='ERP Report Viewer') ,(select id FROM eg_feature WHERE name = 'Departmentwise Drill Down Report'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='Super User'),(select id FROM eg_feature WHERE name = 'Boundarywise Drill Down Report'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='Grievance Officer'),(select id FROM eg_feature WHERE name = 'Boundarywise Drill Down Report'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='Redressal Officer'),(select id FROM eg_feature WHERE name = 'Boundarywise Drill Down Report'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='ERP Report Viewer'),(select id FROM eg_feature WHERE name = 'Boundarywise Drill Down Report'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='Super User') ,(select id FROM eg_feature WHERE name = 'Grievance Typewise Report'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='ERP Report Viewer') ,(select id FROM eg_feature WHERE name = 'Grievance Typewise Report'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='Super User') ,(select id FROM eg_feature WHERE name = 'Functionarywise Report'));

----------------------------------------------------End eg_feature_role------------------------------------------------------------
