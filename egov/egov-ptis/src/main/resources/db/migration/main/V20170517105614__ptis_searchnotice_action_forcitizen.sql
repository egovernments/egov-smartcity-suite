--citizen Notice action
insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,
lastmodifiedby,lastmodifieddate,application) 
values (nextval('SEQ_EG_ACTION'),'citizenNoticeSearch','/reports/searchNotices-citizen',null,
(select id from eg_module where name='PTIS-Reports'),1,'Citizen Notice Search',false,'ptis',0,1,
now(),1,now(),(select id from eg_module where name='Property Tax'));

insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,
lastmodifiedby,lastmodifieddate,application) 
values (nextval('SEQ_EG_ACTION'),'citizenSearch','/reports/searchNotices-citizenSearch',null,
(select id from eg_module where name='PTIS-Reports'),1,'Citizen Search',false,'ptis',0,1,
now(),1,now(),(select id from eg_module where name='Property Tax'));

insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,
lastmodifiedby,lastmodifieddate,application) 
values (nextval('SEQ_EG_ACTION'),'citizenNotice','/reports/citizen-searchnotices',null,
(select id from eg_module where name='PTIS-Reports'),1,'Citizen Notice',false,'ptis',0,1,
now(),1,now(),(select id from eg_module where name='Property Tax'));

insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,
lastmodifiedby,lastmodifieddate,application) 
values (nextval('SEQ_EG_ACTION'),'searchResult','/reports/searchnotice-result',null,
(select id from eg_module where name='PTIS-Reports'),1,'Search Result',false,'ptis',0,1,
now(),1,now(),(select id from eg_module where name='Property Tax'));


