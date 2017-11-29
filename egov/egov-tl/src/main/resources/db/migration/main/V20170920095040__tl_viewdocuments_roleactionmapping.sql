
--View Support Documents 
INSERT INTO eg_action( id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot,
version, createdby, createddate, lastmodifiedby, lastmodifieddate, application)
VALUES ( nextval('seq_eg_action'),'View Support Documents','/license/document',null,
(select id from eg_module where name='Trade License'),'0','View Support Documents',false,'tl',
0,'1',now(),'1',now(),(select id from eg_module where name='Trade License'));

insert into eg_feature_action (action,feature) values ((select id from eg_action where name='View Support Documents'),
(select id from eg_feature  where name='Create Legacy License'));

insert into eg_feature_action (action,feature) values ((select id from eg_action where name='View Support Documents'),
(select id from eg_feature  where name='Search Trade License'));

insert into eg_feature_action (action,feature) values ((select id from eg_action where name='View Support Documents'),
(select id from eg_feature  where name='View Trade'));

insert into eg_feature_action (action,feature) values ((select id from eg_action where name='View Support Documents'),
(select id from eg_feature  where name='Base Register Report'));

insert into eg_feature_action (action,feature) values ((select id from eg_action where name='View Support Documents'),
(select id from eg_feature  where name='License DCB Report'));

insert into eg_feature_action (action,feature) values ((select id from eg_action where name='View Support Documents'),
(select id from eg_feature  where name='Closure'));

insert into eg_feature_action (action,feature) values ((select id from eg_action where name='View Support Documents'),
(select id from eg_feature  where name='License Yearwise DCB Report'));

insert into eg_feature_action (action,feature) values ((select id from eg_action where name='View Support Documents'),
(select id from eg_feature  where name='License Online Payment'));

insert into eg_feature_action (action,feature) values ((select id from eg_action where name='View Support Documents'),
(select id from eg_feature  where name='License Closure Approval'));

insert into eg_roleaction  (actionid,roleid) select (select id from eg_action where name='View Support Documents') ,
role from eg_feature_role  where feature in (select id from eg_feature  where name='Search Trade License');

insert into eg_roleaction  (actionid,roleid) values ((select id from eg_action where name='View Support Documents'),(select id from eg_role where name ='PUBLIC'));

