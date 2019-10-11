-------Actions------
Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,
lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'Court Verdict','/search/searchproperty-courtverdict.action',null, (select id from eg_module where name='Existing property'),1,'View Court Verdict',false,'ptis',0,1, now(),1, now(),(select id from eg_module where name='Property Tax'));

Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,
lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'View Court Verdict','/courtverdict/viewform',null, (select id from eg_module where name='Existing property'),1,'Court Verdict View',false,'ptis', 0, 1, now(), 1, now(), (select id from eg_module where name='Property Tax'));

Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,
lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'Court Verdict Update','/courtverdict/update/',null, (select id from eg_module where name='Existing property'),1,'Update Court Verdict',false,'ptis',0,1, now(),1, now(),(select id from eg_module where name='Property Tax'));

INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate,application) 
VALUES (nextval('seq_eg_action'), 'CategoryByPropertyTypeAjax', '/common/getcategorybypropertytype', null,(select id from eg_module where name='Existing property'), 2, 'CategoryByPropertyTypeAjax', false, 'ptis', 0, 1, now(), 1, now(),(select id from eg_module where name='Property Tax' and parentmodule is null));

---------Role-----
INSERT INTO eg_role(id, name, description, createddate, createdby, lastmodifiedby, lastmodifieddate, version, internal) VALUES (nextval('SEQ_EG_ROLE'), 'Court Case Initiator', 'Court Case Initiator', now(), 1, 1, now(), 0, false);

INSERT INTO eg_role(id, name, description, createddate, createdby, lastmodifiedby, lastmodifieddate, version, internal) VALUES (nextval('SEQ_EG_ROLE'), 'Court Case Approver', 'Court Case Approver', now(), 1, 1, now(), 0, false);

-----Role Action Mappings-------
insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name in ('View Court Verdict')), (select id from eg_role where name in ('Court Case Initiator'));

insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name in ('Court Verdict')), (select id from eg_role where name in ('Court Case Initiator'));

insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name in ('Court Verdict Update')), (select id from eg_role where name in ('Court Case Approver'));

INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name in ('CategoryByPropertyTypeAjax')), id from eg_role where name in ('Court Case Initiator','Court Case Approver');



