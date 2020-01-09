-----------role action mapping for Red notice
INSERT INTO EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,
lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'Red Notice','/rednotice/search',null,(select id from eg_module where name='Existing property'),1,'Red Notice',false,'ptis',0,1,
now(),1,now(),(select id from eg_module where name='Property Tax'));


INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Red Notice'),id from eg_role where name in ('Property Approver');

INSERT INTO EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,
lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'View Red Notice','/rednotice/result',null,(select id from eg_module where name='Existing property'),1,'view Red Notice',false,'ptis',0,1,
now(),1,now(),(select id from eg_module where name='Property Tax'));


INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'View Red Notice'),id from eg_role where name in ('Property Approver');

INSERT INTO EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application)
values (nextval('SEQ_EG_ACTION'),'Genearte Red Notice','/rednotice/generatenotice',null,(select id from eg_module  where name='Existing property'),5,'Genearte Red Notice',false,'ptis',0,1,now(),1,now(),(select id from eg_module where name='Property Tax'));

INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Genearte Red Notice'),id from eg_role where name in ('Property Approver');




