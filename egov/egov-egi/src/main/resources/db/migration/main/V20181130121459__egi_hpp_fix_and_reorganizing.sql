ALTER TABLE eg_action ALTER COLUMN queryparamregex TYPE VARCHAR(150);

UPDATE eg_action SET queryparamregex='stateId=\d+&_=\d+' WHERE name='InboxHistory';

UPDATE eg_action SET name='CHECK CHILD BOUNDARYTYPE EXIST', url='/boundarytype/has-child-boundarytype', 
queryparamregex='parentId=\d+', displayname='CHECK CHILD BOUNDARYTYPE EXIST' WHERE name='AjaxAddChildBoundaryTypeCheck';

UPDATE eg_action SET name='BOUNDARYTYPE BY HIERARCHYTYPE', url='/boundarytype/by-hierarchy', 
queryparamregex='hierarchyTypeId=\d+', displayname='BOUNDARYTYPE BY HIERARCHYTYPE' WHERE name='AjaxLoadBoundaryTypes';

UPDATE eg_action SET queryparamregex='actionId=\d+' WHERE name='RemoveFavourite';

UPDATE eg_action SET queryparamregex='boundaryTypeId=\d+' WHERE name='BoundaryByBoundaryType';

INSERT INTO eg_roleaction
(roleid, actionid)
SELECT rl.roleid, act.id
FROM (SELECT roleid FROM eg_roleaction WHERE actionid=(SELECT id from eg_action WHERE name='AjaxLoadBoundarys')) rl,
(SELECT id FROM eg_action WHERE name='BoundaryByBoundaryType') act
WHERE NOT EXISTS (SELECT 1
              FROM eg_roleaction
              WHERE rl.roleid = roleid AND act.id=actionid);
              
DELETE FROM eg_roleaction WHERE actionid=(SELECT id from eg_action WHERE name='AjaxLoadBoundarys');

UPDATE eg_feature_action SET action=(SELECT id FROM eg_action WHERE name='BoundaryByBoundaryType') 
WHERE action=(SELECT id FROM eg_action WHERE name='AjaxLoadBoundarys');

DELETE FROM eg_action WHERE name='AjaxLoadBoundarys';

UPDATE eg_action SET name='EMPLOYEE BY NAME',
queryparamregex='^name=([[a-zA-Z]+([. a-zA-Z]+)]{2,100})$' 
WHERE name='Employee By Name';

UPDATE eg_action SET url='/user/name-like/', name='USER BY NAME',
queryparamregex='^name=([[a-zA-Z]+([. a-zA-Z]+)]{2,100})$' 
WHERE name='User By Username';

UPDATE eg_action SET queryparamregex='^userName=(?:[\w+( \w+)*]{2,64}|[[\w!#$%&''*+/=?`{|}~^-]+(?:\.[\w!#$%&''*+/=?`{|}~^-]+)*]{2,100}@(?:[a-zA-Z0-9-]{1,22}+\.)+[a-zA-Z]{2,6})$' 
WHERE name='EMPLOYEE BY USERNAME';

UPDATE eg_action SET queryparamregex='^userName=(?:[\w+( \w+)*]{2,64}|[[\w!#$%&''*+/=?`{|}~^-]+(?:\.[\w!#$%&''*+/=?`{|}~^-]+)*]{2,100}@(?:[a-zA-Z0-9-]{1,22}+\.)+[a-zA-Z]{2,6})$' 
WHERE name='EMPLOYEE ROLE BY USERNAME';

UPDATE eg_action SET url='/boundary/block/by-locality', name='BLOCK BY LOCALITY', queryparamregex='locality=\d+' 
WHERE name='Load Block By Locality';

UPDATE eg_action SET url='/boundary/block/by-ward', name='BLOCK BY WARD', queryparamregex='wardId=\d+' 
WHERE name='Load Block By Ward';

UPDATE eg_action SET url='/boundary/ward/by-locality', name='WARD BY LOCALITY', queryparamregex='locality=\d+' 
WHERE name='getWardsByLocality';