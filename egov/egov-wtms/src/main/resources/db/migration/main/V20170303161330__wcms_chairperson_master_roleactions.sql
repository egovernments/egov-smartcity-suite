INSERT INTO eg_module (id, name, enabled, contextroot, parentmodule, displayname, ordernumber) 
VALUES (nextval('SEQ_EG_MODULE'), 'ChairPersonMaster', true, NULL,(select id from eg_module where name='WaterTaxMasters'), 'Chair Person Master', 13);

INSERT INTO EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'EditChairPersonDetailsScreen','/application/chairperson-edit',null,(select id from eg_module where name='ChairPersonMaster'),3,'Modify Chair Person','true','wtms',0,1,now(),1,now(),(select id from eg_module where name='Water Tax Management'));

INSERT INTO eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Super User'),(select id from eg_action where name='EditChairPersonDetailsScreen'));

INSERT INTO EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'EditChairPersonDetailsScreenValues','/application/chairperson-edit/',null,(select id from eg_module where name='ChairPersonMaster'),2,'Modify Chair Person','false','wtms',0,1,now(),1,now(),(select id from eg_module where name='Water Tax Management'));

INSERT INTO eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Super User'),(select id from eg_action where name='EditChairPersonDetailsScreenValues'));

INSERT INTO EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'ViewChairPersonDetailsScreen','/application/chairperson-view',null,(select id from eg_module where name='ChairPersonMaster'),2,'View Chair Person','true','wtms',0,1,now(),1,now(),(select id from eg_module where name='Water Tax Management'));

INSERT INTO eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Super User'),(select id from eg_action where name='ViewChairPersonDetailsScreen'));


INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View ChairPerson Master','View ChairPerson Master Screen',(select id from eg_module  where name = 'Water Tax Management')); 

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Edit ChairPerson Master','Edit ChairPerson Master Screen',(select id from eg_module  where name = 'Water Tax Management')); 

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ViewChairPersonDetailsScreen') ,(select id FROM eg_feature WHERE name = 'View ChairPerson Master'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'EditChairPersonDetailsScreen') ,(select id FROM eg_feature WHERE name = 'Edit ChairPerson Master'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'EditChairPersonDetailsScreenValues') ,(select id FROM eg_feature WHERE name = 'Edit ChairPerson Master'));


INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'View ChairPerson Master'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Edit ChairPerson Master'));

update eg_action set url='/application/chairperson-create', name='CreateChairPersonScreen', parentmodule = (select id from eg_module where name='ChairPersonMaster' and parentmodule = (select id from eg_module where name='WaterTaxMasters')), ordernumber = 1, createddate=now(),lastmodifieddate=now() where name= 'ChairPersonDetailsScreen';
update eg_action set url='/application/ajax-chairpersontable', parentmodule = (select id from eg_module where name='ChairPersonMaster' and parentmodule = (select id from eg_module where name='WaterTaxMasters')) where name= 'getchairpersontableajax';

CREATE TABLE eg_chairperson_aud (

	id integer not null,
	rev integer not null,
	revtype numeric,
	name character varying(100),
	fromdate date,
	todate date,
	active boolean,
	lastmodifieddate timestamp without time zone,
	lastmodifiedby bigint
);

ALTER TABLE ONLY eg_chairperson_aud ADD CONSTRAINT pk_eg_chairperson_aud PRIMARY KEY (id, rev);

INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version,createdby, createddate, lastmodifiedby, lastmodifieddate,application) VALUES (nextval('seq_eg_action'), 'activechairpersonexistsasongivendate-ajax', '/application/ajax-activeChairPersonExistsAsOnGivenDate', null,(select id from eg_module where name='ChairPersonMaster'), null, 'getActiveChairPersonNameForGivenDate', false,'wtms', 0, 1, now(), 1, now(),(select id from eg_module where name='Water Tax Management'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Super User') ,(select id FROM eg_action  WHERE name = 'activechairpersonexistsasongivendate-ajax'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'activechairpersonexistsasongivendate-ajax') ,(select id FROM eg_feature WHERE name = 'Edit ChairPerson Master'));

--rollback delete from eg_feature_role where feature = (select id FROM eg_feature WHERE name = 'View ChairPerson Master') and role = (select id from eg_role where name = 'Super User');
--rollback delete from eg_feature_action where feature = (select id FROM eg_feature WHERE name = 'View ChairPerson Master') and action = (select id FROM eg_action  WHERE name = 'ViewChairPersonDetailsScreen');
--rollback delete from eg_feature where name='View ChairPerson Master' and module=(select id from eg_module  where name = 'Water Tax Management');
--rollback delete from eg_feature_role where feature = (select id FROM eg_feature WHERE name = 'Edit ChairPerson Master') and role = (select id from eg_role where name = 'Super User');
--rollback delete from eg_feature_action where feature = (select id FROM eg_feature WHERE name = 'Edit ChairPerson Master') and action in  ((select id FROM eg_action  WHERE name = 'EditChairPersonDetailsScreen'), (select id FROM eg_action  WHERE name = 'EditChairPersonDetailsScreenValues'), (select id FROM eg_action  WHERE name = 'activechairpersonexistsasongivendate-ajax'));
--rollback delete from eg_feature where name='Edit ChairPerson Master' and module=(select id from eg_module  where name = 'Water Tax Management');
--rollback delete from eg_roleaction where actionid = (select id from eg_action where name='ViewChairPersonDetailsScreen') and roleid = (select id from eg_role where name = 'Super User');
--rollback delete from eg_roleaction where actionid = (select id from eg_action where name='EditChairPersonDetailsScreen') and roleid = (select id from eg_role where name = 'Super User');
--rollback delete from eg_roleaction where actionid = (select id from eg_action where name='EditChairPersonDetailsScreenValues') and roleid = (select id from eg_role where name = 'Super User');
--rollback delete from eg_action where name = 'ViewChairPersonDetailsScreen' and parentmodule=(select id from eg_module where name='ChairPersonMaster');
--rollback delete from eg_action where name = 'EditChairPersonDetailsScreen' and parentmodule=(select id from eg_module where name='ChairPersonMaster');
--rollback delete from eg_action where name = 'EditChairPersonDetailsScreenValues' and parentmodule=(select id from eg_module where name='ChairPersonMaster');
--rollback delete from eg_roleaction where actionid = (select id from eg_action where name='activechairpersonexistsasongivendate-ajax') and roleid = (select id from eg_role where name = 'Super User');
--rollback delete from eg_action where name = 'activechairpersonexistsasongivendate-ajax' and parentmodule=(select id from eg_module where name='ChairPersonMaster');
--rollback delete from eg_module where name = 'ChairPersonMaster' and parentmodule=(select id from eg_module where name='WaterTaxMasters');
--rollback drop table eg_chairperson_aud  
