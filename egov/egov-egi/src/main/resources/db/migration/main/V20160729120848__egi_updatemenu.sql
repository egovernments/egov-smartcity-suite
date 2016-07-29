update eg_action set displayname='Modify Boundary Type',ordernumber='1' where name='UpdateBoundaryTypeForm';
update eg_action set displayname='Create Sub Boundary Type',ordernumber='3' where name='AddChildBoundaryType';
update eg_action set displayname='Modify Hierarchy Type',ordernumber='1' where name='UpdateHierarchyTypeForm';
update eg_action set displayname='Modify Department',ordernumber='1' where name='UpdateDepartmentForm';
update eg_action set displayname='Create Configuration' where name='CreateAppConfig';
update eg_action set displayname='Modify Configuration' where name='modifyAppConfig';
update eg_action set displayname='View Configuration' where name='viewAppConfig';
update eg_action set displayname='Modify Role',ordernumber='1' where name='UpdateRoleForm';
update eg_action set parentmodule=(select id from eg_module  where name='Boundary') where name='modifyCrossHierarchy';
update eg_module set displayname='User Role Assignment' where name='Role Mapping';

update eg_action set ordernumber='0' where name='CreateHierarchyTypeForm';
update eg_action set ordernumber='2' where name='ViewHierarchyTypeForm';
update eg_action set ordernumber='0' where name='CreateBoundaryTypeForm';
update eg_action set ordernumber='2' where name='ViewBoundaryTypeForm';
update eg_action set ordernumber='0' where name='CreateRoleForm';
update eg_action set ordernumber='2' where name='ViewRoleForm';
update eg_action set ordernumber='0' where name='CreateDepartmentForm';
update eg_action set ordernumber='2' where name='ViewDepartmentForm';
