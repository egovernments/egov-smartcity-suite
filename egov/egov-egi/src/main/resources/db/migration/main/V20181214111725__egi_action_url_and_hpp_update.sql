UPDATE eg_action SET name='ADMIN_BOUNDARY_BY_BOUNDARY_TYPE', url='/boundary/by-boundarytype',
queryparamregex='^boundaryName=[\w\s()/-]{1,512}+&boundaryTypeId=\d+$' WHERE name='BoundaryByBoundaryType';

INSERT INTO eg_action(id,name,url,parentmodule,ordernumber,displayname, enabled, contextroot,version,
                     createdby,createddate,lastmodifiedby,lastmodifieddate,application) 
VALUES 
(NEXTVAL('SEQ_EG_ACTION'),'BOUNDARY_BY_NAME_AND_BOUNDARY_TYPE','/boundary/by-name-and-type',
 (SELECT id FROM eg_module WHERE name='Boundary Module'),0,'BOUNDARY_BY_NAME_AND_BOUNDARY_TYPE',false,
 'egi',0,(select id from eg_user where username='system'),CURRENT_TIMESTAMP,
 (select id from eg_user where username='system'),CURRENT_TIMESTAMP,(SELECT id FROM eg_module WHERE name='Administration'));