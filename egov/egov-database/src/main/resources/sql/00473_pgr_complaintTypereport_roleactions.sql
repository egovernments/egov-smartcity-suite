

INSERT INTO eg_module(id, name,  enabled, parentmodule, displayname, ordernumber)
 VALUES (nextval('seq_eg_module'), 'Complaint Type Wise Report',  true, 
 (Select id from eg_module where name='Pgr Reports'), 
 'Complaint Type Wise Report', 3);
 
INSERT INTO eg_action(id, name,   CREATEDDATE,LASTMODIFIEDDATE, url, queryparams,PARENTMODULE, ordernumber, displayname, 
ENABLED,  contextroot,application) VALUES (nextval('seq_eg_action'), 'AjaxCallInReportForcomplaintType',  now(),  now(), '/complaint/pgrreport/complaintTypes', 
null, (SELECT id FROM eg_module WHERE name='Complaint Type Wise Report'), null, 'AjaxCallInReportForcomplaintType', false,  'pgr',
(Select id from eg_module where name='PGR' and parentmodule is null));

insert into eg_roleaction (Actionid,roleid)
values((select id from eg_action where name='AjaxCallInReportForcomplaintType'),(Select id from eg_role where name='Super User'));

INSERT INTO eg_action(id, name,   CREATEDDATE,LASTMODIFIEDDATE, url, queryparams,PARENTMODULE, ordernumber, displayname, 
ENABLED,  contextroot,application) VALUES (nextval('seq_eg_action'), 'Complaint Type Wise Report',  now(),  now(), '/report/complaintTypeReport', 
null, (SELECT id FROM eg_module WHERE name='Complaint Type Wise Report'), null, 'Search By Complaint Type', true,  'pgr',
(Select id from eg_module where name='PGR' and parentmodule is null));

insert into eg_roleaction (Actionid,roleid)
values((select id from eg_action where name='Complaint Type Wise Report'),(Select id from eg_role where name='Super User'));

 INSERT INTO eg_action(id, name,   CREATEDDATE,LASTMODIFIEDDATE, url, queryparams,PARENTMODULE, ordernumber, displayname, 
ENABLED,  contextroot,application) VALUES (nextval('seq_eg_action'), 'Complaint Type Wise Report search result',  now(),  now(), '/report/complaintTypeReport/resultList-update', 
null, (SELECT id FROM eg_module WHERE name='Complaint Type Wise Report'), null, 'Complaint Type Wise Report search result', false,  'pgr',
(Select id from eg_module where name='PGR' and parentmodule is null));

insert into eg_roleaction (Actionid,roleid)
values((select id from eg_action where name='Complaint Type Wise Report search result'),(Select id from eg_role where name='Super User'));
