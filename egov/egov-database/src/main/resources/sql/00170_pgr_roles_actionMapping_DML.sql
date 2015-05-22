--Grievance Adminstrator
--complaintType master

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='Add Complaint Type'),(Select id from eg_role where name='Grivance Administrator'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='Add Complaint Type' and context_root='pgr') and roleid='Grivance Administrator';

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='UpdateComplaintType'),(Select id from eg_role where name='Grivance Administrator'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='UpdateComplaintType' and context_root='pgr') and roleid='Grivance Administrator';

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='ViewComplaintType'),(Select id from eg_role where name='Grivance Administrator'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='ViewComplaintType' and context_root='pgr') and roleid='Grivance Administrator';

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='viewComplaintTypeResult'),(Select id from eg_role where name='Grivance Administrator'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='viewComplaintTypeResult' and context_root='pgr') and roleid='Grivance Administrator';

--router master

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='Create Router'),(Select id from eg_role where name='Grivance Administrator'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='Create Router' and context_root='pgr') and roleid='Grivance Administrator';


insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='AjaxRouterBoundariesbyType'),(Select id from eg_role where name='Grivance Administrator'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='AjaxRouterBoundariesbyType' and context_root='pgr') and roleid='Grivance Administrator';

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='AjaxRouterPosition'),(Select id from eg_role where name='Grivance Administrator'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='AjaxRouterPosition' and context_root='pgr') and roleid='Grivance Administrator';

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='Update Router'),(Select id from eg_role where name='Grivance Administrator'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='Update Router' and context_root='pgr') and roleid='Grivance Administrator';

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='Delete Router'),(Select id from eg_role where name='Grivance Administrator'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='Delete Router' and context_root='pgr') and roleid='Grivance Administrator';

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='View Router'),(Select id from eg_role where name='Grivance Administrator'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='View Router' and context_root='pgr') and roleid='Grivance Administrator';

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='Search viewRouter Result'),(Select id from eg_role where name='Grivance Administrator'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='Search viewRouter Result' and context_root='pgr') and roleid='Grivance Administrator';

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='RouterView'),(Select id from eg_role where name='Grivance Administrator'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='RouterView' and context_root='pgr') and roleid='Grivance Administrator';

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='Edit Router'),(Select id from eg_role where name='Grivance Administrator'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='Edit Router' and context_root='pgr') and roleid='Grivance Administrator';

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='Search updateRouter Result'),(Select id from eg_role where name='Grivance Administrator'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='Search updateRouter Result' and context_root='pgr') and roleid='Grivance Administrator';

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='update RouterViaSearch'),(Select id from eg_role where name='Grivance Administrator'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='update RouterViaSearch' and context_root='pgr') and roleid='Grivance Administrator';


insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='AjaxRouterComplaintType'),(Select id from eg_role where name='Grivance Administrator'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='AjaxRouterComplaintType' and context_root='pgr') and roleid='Grivance Administrator';


--jurisdiction masters

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='CreateRoleForm'),(Select id from eg_role where name='Grivance Administrator'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='CreateRoleForm' and context_root='pgr') and roleid='Grivance Administrator';

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='ViewRoleForm'),(Select id from eg_role where name='Grivance Administrator'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='ViewRoleForm' and context_root='pgr') and roleid='Grivance Administrator';

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='UpdateRoleForm'),(Select id from eg_role where name='Grivance Administrator'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='UpdateRoleForm' and context_root='pgr') and roleid='Grivance Administrator';

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='CreateHierarchyTypeForm'),(Select id from eg_role where name='Grivance Administrator'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='CreateHierarchyTypeForm' and context_root='pgr') and roleid='Grivance Administrator';

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='ViewHierarchyTypeForm'),(Select id from eg_role where name='Grivance Administrator'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='ViewHierarchyTypeForm' and context_root='pgr') and roleid='Grivance Administrator';

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='UpdateHierarchyTypeForm'),(Select id from eg_role where name='Grivance Administrator'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='UpdateHierarchyTypeForm' and context_root='pgr') and roleid='Grivance Administrator';

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='CreateBoundaryTypeForm'),(Select id from eg_role where name='Grivance Administrator'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='CreateBoundaryTypeForm' and context_root='pgr') and roleid='Grivance Administrator';

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='ViewBoundaryTypeForm'),(Select id from eg_role where name='Grivance Administrator'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='ViewBoundaryTypeForm' and context_root='pgr') and roleid='Grivance Administrator';

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='CreateBoundary'),(Select id from eg_role where name='Grivance Administrator'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='CreateBoundary' and context_root='pgr') and roleid='Grivance Administrator';

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='AjaxAddChildBoundaryTypeCheck'),(Select id from eg_role where name='Grivance Administrator'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='AjaxAddChildBoundaryTypeCheck' and context_root='pgr') and roleid='Grivance Administrator';

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='AjaxLoadBoundaryTypes'),(Select id from eg_role where name='Grivance Administrator'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='AjaxLoadBoundaryTypes' and context_root='pgr') and roleid='Grivance Administrator';

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='CreateBoundaryForm'),(Select id from eg_role where name='Grivance Administrator'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='CreateBoundaryForm' and context_root='pgr') and roleid='Grivance Administrator';

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='ViewBoundary'),(Select id from eg_role where name='Grivance Administrator'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='ViewBoundary' and context_root='pgr') and roleid='Grivance Administrator';

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='UpdateBoundary'),(Select id from eg_role where name='Grivance Administrator'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='UpdateBoundary' and context_root='pgr') and roleid='Grivance Administrator';

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='Role View'),(Select id from eg_role where name='Grivance Administrator'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='Role View' and context_root='pgr') and roleid='Grivance Administrator';

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='Role Update'),(Select id from eg_role where name='Grivance Administrator'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='Role Update' and context_root='pgr') and roleid='Grivance Administrator';

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='ViewDepartmentForm'),(Select id from eg_role where name='Grivance Administrator'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='ViewDepartmentForm' and context_root='pgr') and roleid='Grivance Administrator';

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='UpdateDepartmentForm'),(Select id from eg_role where name='Grivance Administrator'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='UpdateDepartmentForm' and context_root='pgr') and roleid='Grivance Administrator';

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='ViewuserRoleForm'),(Select id from eg_role where name='Grivance Administrator'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='ViewuserRoleForm' and context_root='pgr') and roleid='Grivance Administrator';

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='UpdateBoundaryTypeForm'),(Select id from eg_role where name='Grivance Administrator'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='UpdateBoundaryTypeForm' and context_root='pgr') and roleid='Grivance Administrator';

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='AddChildBoundaryType'),(Select id from eg_role where name='Grivance Administrator'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='AddChildBoundaryType' and context_root='pgr') and roleid='Grivance Administrator';

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='CreateDepartmentForm'),(Select id from eg_role where name='Grivance Administrator'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='CreateDepartmentForm' and context_root='pgr') and roleid='Grivance Administrator';

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='CreateDepartmentForm'),(Select id from eg_role where name='Grivance Administrator'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='CreateDepartmentForm' and context_root='pgr') and roleid='Grivance Administrator';

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='ViewBoundaryForm'),(Select id from eg_role where name='Grivance Administrator'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='ViewBoundaryForm' and context_root='pgr') and roleid='Grivance Administrator';


insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='UpdateuserRoleForm'),(Select id from eg_role where name='Grivance Administrator'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='UpdateuserRoleForm' and context_root='pgr') and roleid='Grivance Administrator';


insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='UpdateuserRole'),(Select id from eg_role where name='Grivance Administrator'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='UpdateuserRole' and context_root='pgr') and roleid='Grivance Administrator';

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='searchUserRoleForm'),(Select id from eg_role where name='Grivance Administrator'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='searchUserRoleForm' and context_root='pgr') and roleid='Grivance Administrator';

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='viewuserRole'),(Select id from eg_role where name='Grivance Administrator'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='viewuserRole' and context_root='pgr') and roleid='Grivance Administrator';


insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='AjaxLoadRoleByUser'),(Select id from eg_role where name='Grivance Administrator'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='AjaxLoadRoleByUser' and context_root='pgr') and roleid='Grivance Administrator';


--Grievance officer

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='View Complaint'),(Select id from eg_role where name='Grievance Officer'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='View Complaint' and context_root='pgr') and roleid='Grievance Officer';

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='ComplaintRegisterationOfficials'),(Select id from eg_role where name='Grievance Officer'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='ComplaintRegisterationOfficials' and context_root='pgr') and roleid='Grievance Officer';

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='ComplaintTypeAjaxOfficials'),(Select id from eg_role where name='Grievance Officer'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='ComplaintTypeAjaxOfficials' and context_root='pgr') and roleid='Grievance Officer';

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='ComplaintSaveOfficials'),(Select id from eg_role where name='Grievance Officer'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='ComplaintSaveOfficials' and context_root='pgr') and roleid='Grievance Officer';

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='ComplaintLocationRequiredOfficials'),(Select id from eg_role where name='Grievance Officer'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='ComplaintLocationRequiredOfficials' and context_root='pgr') and roleid='Grievance Officer';

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='ComplaintLocationsOfficials'),(Select id from eg_role where name='Grievance Officer'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='ComplaintLocationsOfficials' and context_root='pgr') and roleid='Grievance Officer';


insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='RCCRNRequiredOfficials'),(Select id from eg_role where name='Grievance Officer'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='RCCRNRequiredOfficials' and context_root='pgr') and roleid='Grievance Officer';


insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='load Designations'),(Select id from eg_role where name='Grievance Officer'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='load Designations' and context_root='pgr') and roleid='Grievance Officer';

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='load Positions'),(Select id from eg_role where name='Grievance Officer'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='load Positions' and context_root='pgr') and roleid='Grievance Officer';


insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='load Wards'),(Select id from eg_role where name='Grievance Officer'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='load Wards' and context_root='pgr') and roleid='Grievance Officer';

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='SearchComplaintForm'),(Select id from eg_role where name='Grievance Officer'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='SearchComplaintForm' and context_root='pgr') and roleid='Grievance Officer';

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='viewComplaintImages'),(Select id from eg_role where name='Grievance Officer'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='viewComplaintImages' and context_root='pgr') and roleid='Grievance Officer';

---Redressal officer

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='View Complaint'),(Select id from eg_role where name='Redressal Officer'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='View Complaint' and context_root='pgr') and roleid='Redressal Officer';

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='ComplaintRegisterationOfficials'),(Select id from eg_role where name='Redressal Officer'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='ComplaintRegisterationOfficials' and context_root='pgr') and roleid='Redressal Officer';

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='ComplaintTypeAjaxOfficials'),(Select id from eg_role where name='Redressal Officer'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='ComplaintTypeAjaxOfficials' and context_root='pgr') and roleid='Redressal Officer';

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='ComplaintSaveOfficials'),(Select id from eg_role where name='Redressal Officer'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='ComplaintSaveOfficials' and context_root='pgr') and roleid='Redressal Officer';

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='ComplaintLocationRequiredOfficials'),(Select id from eg_role where name='Redressal Officer'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='ComplaintLocationRequiredOfficials' and context_root='pgr') and roleid='Redressal Officer';

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='ComplaintLocationsOfficials'),(Select id from eg_role where name='Redressal Officer'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='ComplaintLocationsOfficials' and context_root='pgr') and roleid='Redressal Officer';


insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='RCCRNRequiredOfficials'),(Select id from eg_role where name='Redressal Officer'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='RCCRNRequiredOfficials' and context_root='pgr') and roleid='Redressal Officer';


insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='load Designations'),(Select id from eg_role where name='Redressal Officer'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='load Designations' and context_root='pgr') and roleid='Grievance Officer';

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='load Positions'),(Select id from eg_role where name='Redressal Officer'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='load Positions' and context_root='pgr') and roleid='Redressal Officer';


insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='load Wards'),(Select id from eg_role where name='Redressal Officer'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='load Wards' and context_root='pgr') and roleid='Redressal Officer';

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='SearchComplaintForm'),(Select id from eg_role where name='Redressal Officer'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='SearchComplaintForm' and context_root='pgr') and roleid='Redressal Officer';


insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='viewComplaintImages'),(Select id from eg_role where name='Redressal Officer'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='viewComplaintImages' and context_root='pgr') and roleid='Redressal Officer';


--Grievance Operator

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='View Complaint'),(Select id from eg_role where name='Grievance Operator'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='View Complaint' and context_root='pgr') and roleid='Grievance Operator';

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='ComplaintRegisterationOfficials'),(Select id from eg_role where name='Grievance Operator'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='ComplaintRegisterationOfficials' and context_root='pgr') and roleid='Grievance Operator';

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='ComplaintTypeAjaxOfficials'),(Select id from eg_role where name='Grievance Operator'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='ComplaintTypeAjaxOfficials' and context_root='pgr') and roleid='Grievance Operator';

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='ComplaintSaveOfficials'),(Select id from eg_role where name='Grievance Operator'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='ComplaintSaveOfficials' and context_root='pgr') and roleid='Grievance Operator';

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='ComplaintLocationRequiredOfficials'),(Select id from eg_role where name='Grievance Operator'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='ComplaintLocationRequiredOfficials' and context_root='pgr') and roleid='Grievance Operator';

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='ComplaintLocationsOfficials'),(Select id from eg_role where name='Grievance Operator'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='ComplaintLocationsOfficials' and context_root='pgr') and roleid='Grievance Operator';


insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='RCCRNRequiredOfficials'),(Select id from eg_role where name='Grievance Operator'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='RCCRNRequiredOfficials' and context_root='pgr') and roleid='Grievance Operator';


insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='load Designations'),(Select id from eg_role where name='Grievance Operator'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='load Designations' and context_root='pgr') and roleid='Grievance Operator';

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='load Positions'),(Select id from eg_role where name='Grievance Operator'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='load Positions' and context_root='pgr') and roleid='Grievance Operator';


insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='load Wards'),(Select id from eg_role where name='Grievance Operator'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='load Wards' and context_root='pgr') and roleid='Grievance Operator';

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='SearchComplaintForm'),(Select id from eg_role where name='Grievance Operator'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='SearchComplaintForm' and context_root='pgr') and roleid='Grievance Operator';


insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='viewComplaintImages'),(Select id from eg_role where name='Grievance Operator'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='viewComplaintImages' and context_root='pgr') and roleid='Grievance Operator';
