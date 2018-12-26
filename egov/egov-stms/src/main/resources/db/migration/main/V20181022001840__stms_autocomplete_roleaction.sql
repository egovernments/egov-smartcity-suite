insert into eg_action(id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'Sewerage Autocomplete Search','/search/autocomplete',null,(select id from eg_module where name='SewerageTransactions'),1,'Sewerage Autocomplete Search',false,'stms',0,1,now(),1,now(),(select id from eg_module where name='Sewerage Tax Management'));

insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Sewerage Autocomplete Search'),
id from eg_role where name in ('Super User','STMS_VIEW_ACCESS_ROLE','Sewerage Tax Administrator');

