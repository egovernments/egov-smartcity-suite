update eg_action set url='/user/employee-name-like/',name='Employee By Name',displayname='Employee By Name' where name='User By Name';

insert into EG_ACTION (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version,
 createdby, createddate, lastmodifiedby, lastmodifieddate, application) values
(NEXTVAL('SEQ_EG_ACTION'),'User By Username','/user/username-like/',null,
(select id from eg_module where name like 'User Module')
,0,'User By Username',false,'egi',0,1,now(),1,now(),(select id from eg_module where name='Administration'));

insert into EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name='SYSTEM'),
(select id from eg_action where name='User By Username'));
