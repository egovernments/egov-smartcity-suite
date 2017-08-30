
Insert into eg_appconfig (id,key_name,description,version,createdby,lastmodifiedby,createddate,lastmodifieddate,module) 
values (nextval('SEQ_EG_APPCONFIG'),'Reassignment','Enable/Disable reassignment option', 0,
(select id from eg_user where username='egovernments'),(select id from eg_user where username='egovernments'),
now(),now(),(select id from eg_module where name='Trade License'));
 
 Insert into eg_appconfig_values (id, key_id, effective_from, createdby,lastmodifiedby,createddate,lastmodifieddate,value, version) values 
(nextval('SEQ_EG_APPCONFIG_VALUES'), (select id from eg_appconfig where key_name = 'Reassignment' and
module = (select id from eg_module where name = 'Trade License')),now(), 
(select id from eg_user where username='egovernments'),(select id from eg_user where username='egovernments'),
now(),now(), 'N', 0);

INSERT INTO eg_action( id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot,
version, createdby, createddate, lastmodifiedby, lastmodifieddate, application)
VALUES ( nextval('seq_eg_action'),'Reassignment','/license/reassign',null,
(select id from eg_module where name='Trade License'),'0','Reassignment',false,'tl',
0,'1',now(),'1',now(),(select id from eg_module where name='Trade License'));

insert into eg_feature_action (action,feature) values ((select id from eg_action where name='Reassignment'),
(select id from eg_feature  where name='License Approval'));

insert into eg_feature_action (action,feature) values ((select id from eg_action where name='Reassignment'),
(select id from eg_feature  where name='License Closure Approval'));

insert into eg_roleaction  (actionid,roleid) select (select id from eg_action where name='Reassignment') ,
role from eg_feature_role  where feature in (select id from eg_feature  where name='License Approval');
