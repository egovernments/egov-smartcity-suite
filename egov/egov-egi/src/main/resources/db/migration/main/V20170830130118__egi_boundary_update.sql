
update eg_action set url='/boundary/search', name='SearchBoundary', ordernumber=3 where name='SearchBoundaryForm';

update eg_action set ordernumber=1, enabled=true,parentmodule=(select id from eg_module where name='Boundary') where name='CreateBoundary';

update eg_action set url='/boundary/view',parentmodule=(select id from eg_module where name='Boundary') where name='ViewBoundary';

update eg_action set url='/boundary/update/',enabled=true,ordernumber=2,displayname='Modify Boundary',parentmodule=(select id from eg_module where name='Boundary') where name='UpdateBoundary';

update eg_action set name='BoundaryByBoundaryType', url='/boundary/search/by-boundarytype',parentmodule=(select id from eg_module where name='Boundary') where name='ListBoundariesForm';

update eg_action set ordernumber=4,name='CrossHierarchy' where name='modifyCrossHierarchy';

delete from eg_feature_action  where action = (select id from eg_action where name = 'ShowCreateBoundary');

delete from eg_roleaction  where actionid = (select id from eg_action where name ='ShowCreateBoundary');

delete from eg_action where name ='ShowCreateBoundary';

--Create Boundary Feature
insert into eg_feature values(nextval('seq_eg_feature'), 'Create Boundary', 'Create a Boundary', (select id from eg_module  where name='Administration'), 0);

insert into eg_feature_action (feature,action) (select f.id,a.id from eg_feature f cross join eg_action a where f.name='Create Boundary' and a.name in ('CreateBoundary', 'AjaxLoadBoundaryTypes', 'ViewBoundary'));

insert into eg_feature_role (feature, role) (select f.id,fr.role from eg_feature f cross join eg_feature_role fr where f.name='Create Boundary' and fr.feature=(select fe.id from eg_feature fe where fe.name='Add/Modify Boundary'));

--Modify Boundary Feature
insert into eg_feature values(nextval('seq_eg_feature'), 'Modify Boundary', 'Modify a Boundary Detail', (select id from eg_module  where name='Administration'), 0);

insert into eg_feature_action (feature,action) (select f.id,a.id from eg_feature f cross join eg_action a where f.name='Modify Boundary' and a.name in ('UpdateBoundary', 'AjaxLoadBoundaryTypes', 'ViewBoundary','BoundaryByBoundaryType'));

insert into eg_feature_role (feature, role) (select f.id,fr.role from eg_feature f cross join eg_feature_role fr where f.name='Modify Boundary' and fr.feature=(select fe.id from eg_feature fe where fe.name='Add/Modify Boundary'));

--Search Boundary Feature
insert into eg_feature values(nextval('seq_eg_feature'), 'Search Boundary', 'Search for Boundary', (select id from eg_module  where name='Administration'), 0);

insert into eg_feature_action (feature,action) (select f.id,a.id from eg_feature f cross join eg_action a where f.name='Search Boundary' and a.name in ('SearchBoundary', 'AjaxLoadBoundaryTypes', 'ViewBoundary'));

insert into eg_feature_role (feature, role) (select f.id,fr.role from eg_feature f cross join eg_feature_role fr where f.name='Search Boundary' and fr.feature=(select fe.id from eg_feature fe where fe.name='Add/Modify Boundary'));

--delete old boundary feature
delete from eg_feature_role where feature = (select fe.id from eg_feature fe where fe.name='Add/Modify Boundary');
delete from eg_feature_action where feature = (select fe.id from eg_feature fe where fe.name='Add/Modify Boundary');
delete from eg_feature fe where fe.name='Add/Modify Boundary';
