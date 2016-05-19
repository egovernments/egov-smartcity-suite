
Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'Demolition','/search/searchProperty-commonForm.action ','applicationType=Demolition',361,null,'Demolition',false,'ptis',0,1,now(),1,now(),(select id from eg_module where name='Property Tax'));

insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Demolition'), id from eg_role where name in ('ULB Operator','Super User');

Insert into EG_ACTION(id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'Edit Owner Details Form','/search/searchProperty-commonForm.action ','applicationType=Edit_owner',361,null,'Edit Owner Details',false,'ptis',0,1,now(),1,now(),(select id from eg_module where name='Property Tax'));


insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Edit Owner Details Form'), id from eg_role where name in ('Property Approver');

insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name='Assessment-commonSearch'), id from eg_role where name in ('Property Approver');






