
INSERT INTO eg_module(id, name,  enabled, parentmodule, displayname, ordernumber)
 VALUES (nextval('seq_eg_module'), 'Pgr Reports',  true, 
 (Select id from eg_module where name='PGR' and parentmodule is null), 
 'Reports', 3);


INSERT INTO eg_module(id, name,  enabled, parentmodule, displayname, ordernumber)
 VALUES (nextval('seq_eg_module'), 'Ageing Report',  true, 
 (Select id from eg_module where name='Pgr Reports'), 
 'Ageing Report', 1);
INSERT INTO eg_module(id, name,  enabled, parentmodule, displayname, ordernumber)
 VALUES (nextval('seq_eg_module'), 'Drill Down Report',  true, 
 (Select id from eg_module where name='Pgr Reports'), 
 'Drill Down Report', 2);

INSERT INTO eg_action(id, name,   CREATEDDATE,LASTMODIFIEDDATE, url, queryparams,PARENTMODULE, ordernumber, displayname, 
ENABLED,  contextroot,application) VALUES (nextval('seq_eg_action'), 'Ageing Report By Boundary wise',  now(),  now(), '/report/ageingReportByBoundary', 
null, (SELECT id FROM eg_module WHERE name='Ageing Report'), null, 'By Boundary wise', true,  'pgr',
(Select id from eg_module where name='PGR' and parentmodule is null));


INSERT INTO eg_action(id, name,   CREATEDDATE,LASTMODIFIEDDATE, url, queryparams,PARENTMODULE, ordernumber, displayname, 
ENABLED,  contextroot,application) VALUES (nextval('seq_eg_action'), 'Ageing Report By Department wise',  now(),  now(), '/report/ageingReportByDept', 
null, (SELECT id FROM eg_module WHERE name='Ageing Report'), null, 'By Department wise', true,  'pgr',
(Select id from eg_module where name='PGR' and parentmodule is null));


insert into eg_roleaction (Actionid,roleid)
values((select id from eg_action where name='Ageing Report By Boundary wise'),(Select id from eg_role where name='Super User'));
insert into eg_roleaction (Actionid,roleid)
values((select id from eg_action where name='Ageing Report By Department wise'),(Select id from eg_role where name='Super User'));


 INSERT INTO eg_action(id, name,   CREATEDDATE,LASTMODIFIEDDATE, url, queryparams,PARENTMODULE, ordernumber, displayname, 
ENABLED,  contextroot,application) VALUES (nextval('seq_eg_action'), 'Ageing report search result',  now(),  now(), '/report/ageing/resultList-update', 
null, (SELECT id FROM eg_module WHERE name='Pgr Reports'), null, 'Ageing report search result', false,  'pgr',
(Select id from eg_module where name='PGR' and parentmodule is null));

insert into eg_roleaction (Actionid,roleid)
values((select id from eg_action where name='Ageing report search result'),(Select id from eg_role where name='Super User'));
