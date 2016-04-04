INSERT INTO EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'Search Unit rate','/admin/unitRate-searchForm.action',null,
(select id from eg_module  where name='PTIS-Administration'),1,'Search Unit rate',false,'ptis',0,1,now(),1,now(),(select id from eg_module where name='Property Tax'));

INSERT INTO EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'Search Unit rate Submit','/admin/unitRate-search.action',null,
(select id from eg_module  where name='PTIS-Administration'),1,'Search Unit rate Submit',false,'ptis',0,1,now(),1,now(),(select id from eg_module where name='Property Tax'));

INSERT INTO EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'View Unit rate','/admin/unitRate-view.action',null,
(select id from eg_module  where name='PTIS-Administration'),1,'View Unit rate',false,'ptis',0,1,now(),1,now(),(select id from eg_module where name='Property Tax'));

INSERT INTO EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'Update Unit rate','/admin/unitRate-update.action',null,
(select id from eg_module  where name='PTIS-Administration'),1,' Update Unit rate',false,'ptis',0,1,now(),1,now(),(select id from eg_module where name='Property Tax'));

INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Search Unit rate'),id from eg_role where name in ('Property Approver');

INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Search Unit rate Submit'),id from eg_role where name in ('Property Approver');

INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'View Unit rate'),id from eg_role where name in ('Property Approver');

INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Update Unit rate'),id from eg_role where name in ('Property Approver');


