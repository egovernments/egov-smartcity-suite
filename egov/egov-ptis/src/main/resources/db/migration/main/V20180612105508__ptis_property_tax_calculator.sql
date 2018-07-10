Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,
lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'Calculate Property Tax','/calculatepropertytax',null,
(select id from eg_module where name='PTIS-Masters'),1,'Calculate Property Tax',false,'ptis',0,1,
now(),1,
now(),(select id from eg_module where name='Property Tax'));


