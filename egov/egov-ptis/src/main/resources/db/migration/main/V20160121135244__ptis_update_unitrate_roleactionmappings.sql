INSERT INTO eg_module (id, name, enabled, contextroot, parentmodule, displayname, ordernumber) VALUES (nextval('SEQ_EG_MODULE'), 'Unit Rate Master', true, 'ptis',(select id from eg_module where name='PTIS-Administration'), 'Unit Rate', 1);

UPDATE eg_action set parentmodule=(select id from eg_module where name='Unit Rate Master'),ordernumber = 1,displayname = 'Create' where name='Unit Rate Master'and contextroot='ptis';

UPDATE eg_action set parentmodule=(select id from eg_module where name='Unit Rate Master'),ordernumber = 2,displayname = 'View' ,name = 'Search Unit Rate View',enabled = true,queryparams='mode=view' where name = 'Search Unit rate' and contextroot='ptis';

Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'Search unit rate edit','/admin/unitRate-searchForm.action','mode=edit',
(select id from eg_module where name='Unit Rate Master'),3,'Edit','t','ptis',0,1,now(),1,now(),(select id from eg_module where name='Property Tax'));

insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Search unit rate edit'), id from eg_role where name in ('Property Administrator');