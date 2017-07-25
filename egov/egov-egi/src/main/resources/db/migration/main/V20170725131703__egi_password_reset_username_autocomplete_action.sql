insert into EG_ACTION (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version,
 createdby, createddate, lastmodifiedby, lastmodifieddate, application) values 
(NEXTVAL('SEQ_EG_ACTION'),'User By Name','/user/names',null,
(select id from eg_module where name like 'User Module')
,0,'User By Name',false,'egi',0,1,now(),1,now(),(select id from eg_module where name='Administration'));

insert into EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name='SYSTEM'),
(select id from eg_action where name='User By Name'));

insert into eg_feature_action(feature,action)  values ((select id from eg_feature where name='Reset Password' and module = (select id from eg_module where name ='Administration')), (select id from eg_action where name='User By Name' and application=(select id from eg_module where name='Administration')));

