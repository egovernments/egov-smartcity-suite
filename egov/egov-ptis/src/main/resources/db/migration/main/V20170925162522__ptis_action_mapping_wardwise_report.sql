insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,
lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'Revenue Ward Wise Service Report','/reports/wardWiseServiceTypeReport/countApplications',null,
362,1,'Revenue Ward Wise Service Report',true,'ptis',0,1,
now(),1,now(),(select id from eg_module where name='Property Tax'));

insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Revenue Ward Wise Service Report'),id from eg_role where name  in ('SYSTEM','VIEW_ACCESS_ROLE','ERP Report Viewer');
