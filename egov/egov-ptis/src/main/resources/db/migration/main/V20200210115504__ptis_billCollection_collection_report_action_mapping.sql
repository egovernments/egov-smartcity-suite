INSERT INTO EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,
lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'Bill Collectors Collection Report','/report/billcollectorcollection',null,(select id from eg_module where name='Existing property'),1,'Bill Collectors Collection Report',true,'ptis',0,1,
now(),1,now(),(select id from eg_module where name='Property Tax'));

INSERT INTO EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,
lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'billcollectorcollectionajax','/report/billcollectorcollection/results',null,(select id from eg_module where name='Existing property'),1,'billcollectorcollectionajax',false,'ptis',0,1,
now(),1,now(),(select id from eg_module where name='Property Tax'));


INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Bill Collectors Collection Report'),id from eg_role where name in ('ERP Report Viewer','SYSTEM');

INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'billcollectorcollectionajax'), id from eg_role where name in ('ERP Report Viewer','SYSTEM');


