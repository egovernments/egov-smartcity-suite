INSERT INTO eg_action(id, name,   CREATEDDATE,LASTMODIFIEDDATE, url, queryparams,PARENTMODULE, ordernumber, displayname, 
ENABLED,  contextroot,application) VALUES (nextval('seq_eg_action'), 'complaintTypeSuccess',  now(),  now(), '/complainttype/success', 
null, (SELECT id FROM eg_module WHERE name='Complaint Type'), null, 'complaintTypeSuccess', false,  'pgr',
(Select id from eg_module where name='PGR' and parentmodule is null));


insert into eg_roleaction (Actionid,roleid)
values((select id from eg_action where name='complaintTypeSuccess'),(Select id from eg_role where name='Super User'));

insert into eg_roleaction (Actionid,roleid)
values((select id from eg_action where name='complaintTypeSuccess'),(Select id from eg_role where name=' Grivance Administrator'));


--rollback delete from eg_roleaction where actionid in (select id from eg_action where name='complaintTypeSuccess');
--rollback delete from eg_action where name='complaintTypeSuccess';
