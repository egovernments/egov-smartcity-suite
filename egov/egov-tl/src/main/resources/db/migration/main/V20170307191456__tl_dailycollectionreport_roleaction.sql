Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'Daily Collection Report','/reports/dailycollectionreport',null,(select id from eg_module where name='Trade License Reports'),3,'Daily Collection Report',true,'tl',0,1,now(),1,now(),(select id from eg_module where name='Trade License'));

INSERT INTO EG_ROLEACTION (roleid,actionid) VALUES((select id FROM eg_role where name in ('Super User')),(select id from eg_action where name in('Daily Collection Report')) );

INSERT INTO EG_ROLEACTION (roleid,actionid) VALUES((select id FROM eg_role where name in ('TLAdmin')),(select id from eg_action where name in('Daily Collection Report')) );

INSERT INTO EG_FEATURE (id,name,description,module,version) VALUES (nextval('SEQ_EG_ACTION'),'Daily Collection Report','Daily Collection Report',(select id from eg_module where name='Trade License'),0);


INSERT INTO EG_FEATURE_ACTION (action,feature) VALUES ((select id FROM eg_action WHERE name ='Daily Collection Report'),(select id FROM EG_FEATURE
WHERE name  ='Daily Collection Report'));

INSERT INTO EG_FEATURE_role (role,feature) VALUES ((select id FROM eg_role where name in ('Super User')),(select id FROM EG_FEATURE
WHERE name  ='Daily Collection Report'));

INSERT INTO EG_FEATURE_role (role,feature) VALUES ((select id FROM eg_role where name in ('TLAdmin')),(select id FROM EG_FEATURE
WHERE name  ='Daily Collection Report'));







