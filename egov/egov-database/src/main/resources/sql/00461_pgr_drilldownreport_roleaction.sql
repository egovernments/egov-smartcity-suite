
INSERT INTO eg_action(id, name,   CREATEDDATE,LASTMODIFIEDDATE, url, queryparams,PARENTMODULE, ordernumber, displayname, 
ENABLED,  contextroot,application) VALUES (nextval('seq_eg_action'), 'Drill Down Report By Boundary wise',  now(),  now(), '/report/drillDownReportByBoundary', 
null, (SELECT id FROM eg_module WHERE name='Drill Down Report'), null, 'By Boundary wise', true,  'pgr',
(Select id from eg_module where name='PGR' and parentmodule is null));

INSERT INTO eg_action(id, name,   CREATEDDATE,LASTMODIFIEDDATE, url, queryparams,PARENTMODULE, ordernumber, displayname, 
ENABLED,  contextroot,application) VALUES (nextval('seq_eg_action'), 'Drill Down Report By Department wise',  now(),  now(), '/report/drillDownReportByDept', 
null, (SELECT id FROM eg_module WHERE name='Drill Down Report'), null, 'By Department wise', true,  'pgr',
(Select id from eg_module where name='PGR' and parentmodule is null));

insert into eg_roleaction (Actionid,roleid)
values((select id from eg_action where name='Drill Down Report By Boundary wise'),(Select id from eg_role where name='Super User'));

insert into eg_roleaction (Actionid,roleid)
values((select id from eg_action where name='Drill Down Report By Department wise'),(Select id from eg_role where name='Super User'));

INSERT INTO eg_action(id, name,   CREATEDDATE,LASTMODIFIEDDATE, url, queryparams,PARENTMODULE, ordernumber, displayname, 
ENABLED,  contextroot,application) VALUES (nextval('seq_eg_action'), 'Drill Down Report search result',  now(),  now(), '/report/drillDown/resultList-update', 
null, (SELECT id FROM eg_module WHERE name='Pgr Reports'), null, 'Drill Down Report search result', false,  'pgr',
(Select id from eg_module where name='PGR' and parentmodule is null));

insert into eg_roleaction (Actionid,roleid)
values((select id from eg_action where name='Drill Down Report search result'),(Select id from eg_role where name='Super User'));
