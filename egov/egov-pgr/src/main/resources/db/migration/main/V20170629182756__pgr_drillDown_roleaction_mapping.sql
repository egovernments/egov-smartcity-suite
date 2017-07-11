
--drilldown report roleaction mapping
Insert into EG_ACTION (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version,
 createdby, createddate, lastmodifiedby, lastmodifieddate, application) values 
(NEXTVAL('SEQ_EG_ACTION'),'Drill Down report Grand Total','/report/drilldown/grand-total',null,
(select id from eg_module where name like 'Drill Down Report')
,null,'Drill Down report Grand Total','false','pgr',0,1,now(),1,now(),(select id from eg_module where name='PGR'));

Insert into EG_ACTION (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version,
createdby, createddate, lastmodifiedby, lastmodifieddate, application) values (NEXTVAL('SEQ_EG_ACTION'),
'Drill Down report Download','/report/drilldown/download',null,
(select id from eg_module where name='Drill Down Report'),null,
'Drill Down report Download','false','pgr',0,1,now(),1,now(),(select id from eg_module where name='PGR'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name='Grivance Administrator'),
(select id from eg_action where name='Drill Down report Download'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name='SYSTEM'),
(select id from eg_action where name='Drill Down report Download'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name='PGR VIEW ACCESS'),
(select id from eg_action where name='Drill Down report Download'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name='Grievance Routing Officer'),
(select id from eg_action where name='Drill Down report Download'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name='Redressal Officer'),
(select id from eg_action where name='Drill Down report Download'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name='Grievance Officer'),
(select id from eg_action where name='Drill Down report Download'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name='Grievance Routing Officer'),
(select id from eg_action where name='Drill Down report Grand Total'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name='Grievance Officer'),
(select id from eg_action where name='Drill Down report Grand Total'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name='Redressal Officer'),
(select id from eg_action where name='Drill Down report Grand Total'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name='PGR VIEW ACCESS'),
(select id from eg_action where name='Drill Down report Grand Total'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name='Grivance Administrator'),
(select id from eg_action where name='Drill Down report Grand Total'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name='SYSTEM'),
(select id from eg_action where name='Drill Down report Grand Total'));


INSERT INTO EG_FEATURE_ACTION (action,feature) VALUES ((select id FROM eg_action WHERE name ='Drill Down report Grand Total'),
(select id FROM EG_FEATURE WHERE name  ='Boundarywise Drill Down Report'));

INSERT INTO EG_FEATURE_ACTION (action,feature) VALUES ((select id FROM eg_action WHERE name ='Drill Down report Download'),
(select id FROM EG_FEATURE WHERE name  ='Boundarywise Drill Down Report'));

INSERT INTO EG_FEATURE_ACTION (action,feature) VALUES ((select id FROM eg_action WHERE name ='Drill Down report Grand Total'),
(select id FROM EG_FEATURE WHERE name  ='Departmentwise Drill Down Report'));

INSERT INTO EG_FEATURE_ACTION (action,feature) VALUES ((select id FROM eg_action WHERE name ='Drill Down report Download'),
(select id FROM EG_FEATURE WHERE name  ='Departmentwise Drill Down Report'));
