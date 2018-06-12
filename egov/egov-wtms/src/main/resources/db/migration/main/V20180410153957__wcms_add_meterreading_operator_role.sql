
delete from eg_roleaction where actionid in (select id from eg_action where name in ('createReConnection') and contextroot = 'wtms') and roleid in(select id from eg_role where name = 'CSC Operator');

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='CSC Operator'),
(select id from eg_action where name='createReConnection'));


INSERT INTO EG_ROLE(id,name,description,createddate,createdby,lastmodifiedby,lastmodifieddate,version, internal) VALUES (nextval('seq_eg_role'),'Water Meter Reading Operator','Role for updating water meter reading',now(),1,1,now(),0, false);

INSERT INTO eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Water Meter Reading Operator'),
(select id from eg_action where name='EnterMeterEntryForConnection'));

INSERT INTO eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Water Meter Reading Operator'),
(select id from eg_action where name='meterEntryScreen'));

