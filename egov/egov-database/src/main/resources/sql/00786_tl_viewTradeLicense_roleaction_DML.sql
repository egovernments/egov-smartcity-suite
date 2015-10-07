Delete from eg_roleaction where actionid=(select id from eg_action where name='View Trade License Show for Approval' and contextroot = 'tl');
Delete from eg_action where name='View Trade License Show for Approval' and contextroot = 'tl';

-- Adding roleaction for TLApprover
insert into eg_roleaction (roleid, actionid) select (select id from eg_role where name = 'TLApprover'), id from eg_action where name='tradeLicenseLocalityAjax' and contextroot = 'tl';
insert into eg_roleaction (roleid, actionid) select (select id from eg_role where name = 'TLApprover'), id from eg_action where name = 'newTradeLicense-create';

-- Adding egaction and roleaction for view Trade License
Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'View Trade License for Approval','/newtradelicense/newTradeLicense-showForApproval.action',null,(select id from EG_MODULE where name = 'Trade License'),1,'View Trade License for Approval','f','tl',0,1,now(),1,now(),(select id from eg_module  where name = 'Trade License'));

insert into eg_roleaction (roleid, actionid) select (select id from eg_role where name = 'TLCreator'), id from eg_action where name='View Trade License for Approval' and contextroot = 'tl';
insert into eg_roleaction (roleid, actionid) select (select id from eg_role where name = 'TLApprover'), id from eg_action where name='View Trade License for Approval' and contextroot = 'tl';
insert into eg_roleaction (roleid, actionid) select (select id from eg_role where name = 'Super User'), id from eg_action where name='View Trade License for Approval' and contextroot = 'tl';



