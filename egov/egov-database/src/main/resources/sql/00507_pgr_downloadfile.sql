INSERT INTO eg_action(id, name,   CREATEDDATE,LASTMODIFIEDDATE, url, queryparams,PARENTMODULE, ordernumber, displayname, 
ENABLED,  contextroot,application) VALUES (nextval('seq_eg_action'), 'complaint downloadfile',  now(),  now(), '/complaint/downloadfile', 
null, (SELECT id FROM eg_module WHERE name='PGRComplaints'), null, 'Complaint downloadfile', true,  'pgr',
(Select id from eg_module where name='PGR' and parentmodule is null));

insert into eg_roleaction (Actionid,roleid)
values((select id from eg_action where name='complaint downloadfile'),(Select id from eg_role where name='Citizen'));

insert into eg_roleaction (Actionid,roleid)
values((select id from eg_action where name='complaint downloadfile'),(Select id from eg_role where name='Grievance Officer'));

insert into eg_roleaction (Actionid,roleid)
values((select id from eg_action where name='complaint downloadfile'),(Select id from eg_role where name='Redressal Officer'));

insert into eg_roleaction (Actionid,roleid)
values((select id from eg_action where name='complaint downloadfile'),(Select id from eg_role where name='Grivance Administrator'));

--rollback delete from eg_roleaction where actionid in (select id from eg_action where name='complaint downloadfile');
--rollback delete from eg_action where url='/complaint/downloadfile' and name='complaint downloadfile';
