update eg_action set url='/boundarytype/create' WHERE name='CreateBoundaryTypeForm';
update eg_action set url='/boundarytype/view' WHERE name='ViewBoundaryTypeForm';
update eg_action set url='/boundarytype/update' where name='UpdateBoundaryTypeForm';
update eg_action set url='/boundarytype/addchild' where name='AddChildBoundaryType';
update eg_action set url='/boundarytype/ajax/checkchild' where name='AjaxAddChildBoundaryTypeCheck';
update eg_action set url='/boundarytype/ajax/boundarytypelist-for-hierarchy' where name='AjaxLoadBoundaryTypes';

--rollback update eg_action set url='/create-boundaryType' WHERE name='CreateBoundaryTypeForm';
--rollback update eg_action set url='/boundaryType/view' WHERE name='ViewBoundaryTypeForm';
--rollback update eg_action set url='/boundaryType/update' where name='UpdateBoundaryTypeForm';
--rollback update eg_action set url='/boundaryType/addChild' where name='AddChildBoundaryType';
--rollback update eg_action set url='/addChildBoundaryType/isChildPresent' where name='AjaxAddChildBoundaryTypeCheck';
--rollback update eg_action set url='/boundaryTypes-by-hierarchyType' where name='AjaxLoadBoundaryTypes';