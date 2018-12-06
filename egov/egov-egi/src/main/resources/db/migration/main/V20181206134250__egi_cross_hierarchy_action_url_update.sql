UPDATE eg_action set name='SEARCH CROSS HIERARCHY',url='/crosshierarchy/search',displayname='Define Cross Hierarchy' WHERE name='CrossHierarchy';
INSERT INTO eg_action
(ID,NAME,URL,QUERYPARAMS,QUERYPARAMREGEX,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,
 VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION)
values (NEXTVAL('SEQ_EG_ACTION'),'MODIFY CROSS HIERARCHY','/crosshierarchy/update/',null,null,
        (SELECT id FROM EG_MODULE WHERE name = 'Boundary'),0,'MODIFY CROSS HIERARCHY',FALSE, 
        'egi',0,(select id FROM eg_user WHERE username='system'),now(),
        (SELECT id FROM eg_user WHERE username='system'),now(),
        (SELECT id FROM eg_module WHERE name = 'Administration' AND parentmodule IS NULL));

INSERT INTO EG_ROLEACTION(roleid,actionid)
SELECT ra.roleid, act.id FROM 
(SELECT roleid FROM EG_ROLEACTION WHERE actionid=(SELECT id FROM eg_action WHERE name='SEARCH CROSS HIERARCHY')) ra,
 (SELECT id FROM EG_ACTION WHERE name='MODIFY CROSS HIERARCHY') act;
 
INSERT INTO eg_feature_action(feature,action) SELECT ft.id,act.id FROM 
(SELECT id FROM eg_feature WHERE name='Cross Hierarchy') ft, 
(SELECT id FROM eg_action WHERE name='MODIFY CROSS HIERARCHY') act;
