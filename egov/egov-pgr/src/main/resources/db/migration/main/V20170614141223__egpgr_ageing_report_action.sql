--eg_action for ageing report

Insert into EG_ACTION (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version,
 createdby, createddate, lastmodifiedby, lastmodifieddate, application) values 
(NEXTVAL('SEQ_EG_ACTION'),'Ageing report Grand Total','/report/ageing/grand-total',null,(select id from eg_module where name like 'Pgr Reports')
,null,'Ageing report Grand Total','false','pgr',0,1,'now()',1,'now()',(select id from eg_module where name='PGR'));

Insert into EG_ACTION (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version,
createdby, createddate, lastmodifiedby, lastmodifieddate, application) values (NEXTVAL('SEQ_EG_ACTION'),
'Ageing report Download','/report/ageing/download',null,(select id from eg_module where name='Pgr Reports'),null,
'Ageing report Download','false','pgr',0,1,'now()',1,'now()',(select id from eg_module where name='PGR'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name='Grivance Administrator'),
(select id from eg_action where name='Ageing report Download'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name='SYSTEM'),
(select id from eg_action where name='Ageing report Download'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name='Grivance Administrator'),
(select id from eg_action where name='Ageing report Grand Total'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name='SYSTEM'),
(select id from eg_action where name='Ageing report Grand Total'));


INSERT INTO EG_FEATURE_ACTION (action,feature) VALUES ((select id FROM eg_action WHERE name ='Ageing report Grand Total'),(select id FROM EG_FEATURE
WHERE name  ='Boundarywise Ageing Report'));
INSERT INTO EG_FEATURE_ACTION (action,feature) VALUES ((select id FROM eg_action WHERE name ='Ageing report Download'),(select id FROM EG_FEATURE
WHERE name  ='Boundarywise Ageing Report'));

INSERT INTO EG_FEATURE_ACTION (action,feature) VALUES ((select id FROM eg_action WHERE name ='Ageing report Grand Total'),(select id FROM EG_FEATURE
WHERE name  ='Departmentwise Ageing Report'));
INSERT INTO EG_FEATURE_ACTION (action,feature) VALUES ((select id FROM eg_action WHERE name ='Ageing report Download'),(select id FROM EG_FEATURE
WHERE name  ='Departmentwise Ageing Report'));