
Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,
CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'Create License Closure',
'/license/closure',null,(select id from EG_MODULE where name = 'Trade License Transactions'),1,
'Create License Closure','f','tl',0,1,now(),1,now(),(select id from eg_module  where name = 'Trade License'));

insert into eg_feature_action (action,feature)  values((select id from eg_action where name='Create License Closure'),
(select id from eg_feature  where name='Closure'));

insert into eg_roleaction (actionid,roleid) select distinct (select id from eg_action where name='Create License Closure'),
roleid from eg_roleaction  where actionid  in(select id from eg_action where name='showclosureform');


Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,
CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'View License Success',
'/license/view',null,(select id from EG_MODULE where name = 'Trade License Transactions'),1,
'View License Success','f','tl',0,1,now(),1,now(),(select id from eg_module  where name = 'Trade License'));

insert into eg_feature_action (action,feature)  values((select id from eg_action where name='View License Success'),
(select id from eg_feature  where name='Closure'));

insert into eg_feature_action (action,feature)  values((select id from eg_action where name='View License Success'),
(select id from eg_feature  where name='License Closure Approval'));

insert into eg_roleaction (actionid,roleid) select distinct (select id from eg_action where name='View License Success'),
roleid from eg_roleaction  where actionid  in(select id from eg_action where name='showclosureform');


Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,
CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'Show License Closure',
'/license/closure/update',null,(select id from EG_MODULE where name = 'Trade License Transactions'),1,
'Show License Closure','f','tl',0,1,now(),1,now(),(select id from eg_module  where name = 'Trade License'));

insert into eg_feature_action (action,feature)  values((select id from eg_action where name='Show License Closure'),
(select id from eg_feature  where name='License Closure Approval'));

insert into eg_roleaction (actionid,roleid) select distinct (select id from eg_action where name='Show License Closure'),
roleid from eg_roleaction  where actionid  in(select id from eg_action where name='viewclosurelicense');


Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,
CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'Forward License Closure',
'/license/closure/forward',null,(select id from EG_MODULE where name = 'Trade License Transactions'),1,
'Forward License Closure','f','tl',0,1,now(),1,now(),(select id from eg_module  where name = 'Trade License'));

insert into eg_feature_action (action,feature)  values((select id from eg_action where name='Forward License Closure'),
(select id from eg_feature  where name='License Closure Approval'));

insert into eg_roleaction (actionid,roleid) select distinct (select id from eg_action where name='Forward License Closure'),
roleid from eg_roleaction  where actionid  in(select id from eg_action where name='viewclosurelicense');


Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,
CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'Approve License Closure',
'/license/closure/approve',null,(select id from EG_MODULE where name = 'Trade License Transactions'),1,
'Approve License Closure','f','tl',0,1,now(),1,now(),(select id from eg_module  where name = 'Trade License'));

insert into eg_feature_action (action,feature)  values((select id from eg_action where name='Approve License Closure'),
(select id from eg_feature  where name='License Closure Approval'));

insert into eg_roleaction (actionid,roleid) select distinct (select id from eg_action where name='Approve License Closure'),
roleid from eg_roleaction  where actionid  in(select id from eg_action where name='viewclosurelicense');


Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,
CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'Reject License Closure',
'/license/closure/reject',null,(select id from EG_MODULE where name = 'Trade License Transactions'),1,
'Reject License Closure','f','tl',0,1,now(),1,now(),(select id from eg_module  where name = 'Trade License'));

insert into eg_feature_action (action,feature)  values((select id from eg_action where name='Reject License Closure'),
(select id from eg_feature  where name='License Closure Approval'));

insert into eg_roleaction (actionid,roleid) select distinct (select id from eg_action where name='Reject License Closure'),
roleid from eg_roleaction  where actionid  in(select id from eg_action where name='viewclosurelicense');


Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,
CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'Cancel License Closure',
'/license/closure/cancel',null,(select id from EG_MODULE where name = 'Trade License Transactions'),1,
'Cancel License Closure','f','tl',0,1,now(),1,now(),(select id from eg_module  where name = 'Trade License'));

insert into eg_feature_action (action,feature)  values((select id from eg_action where name='Cancel License Closure'),
(select id from eg_feature  where name='License Closure Approval'));

insert into eg_roleaction (actionid,roleid) select distinct (select id from eg_action where name='Cancel License Closure'),
roleid from eg_roleaction  where actionid  in(select id from eg_action where name='viewclosurelicense');


insert into eg_feature_action (action,feature)  values((select id from eg_action where name='AjaxApproverByDesignationAndDepartment'),
(select id from eg_feature  where name='License Closure Approval'));

insert into eg_feature_action (action,feature)  values((select id from eg_action where name='AjaxDesignationsByDepartmentWithDesignation'),
(select id from eg_feature  where name='License Closure Approval'));