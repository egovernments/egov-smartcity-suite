---------Action for VR Generate Notice----------
Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'VacancyRemission GenerateNotice','/vacancyremission/generatenotice',null,(select id from eg_module where name='PTIS-Masters'),1,'Generate VR Special Notice',false,'ptis',0,1,now(),1,now(),(select id from eg_module where name='Property Tax'));

----------role action----------------------------
insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'VacancyRemission GenerateNotice' and contextroot='ptis'), id from eg_role where name in ('Property Approver');
