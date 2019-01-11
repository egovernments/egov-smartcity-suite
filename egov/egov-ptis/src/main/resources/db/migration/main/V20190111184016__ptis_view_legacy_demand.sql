Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,
lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'View Legacy Demand','/view/viewDCBProperty-legacyDemand.action',null,
(select id from eg_module where name='Existing property'),1,'View Legacy Demand',false,'ptis',0,1,
now(),1,
now(),(select id from eg_module where name='Property Tax'));