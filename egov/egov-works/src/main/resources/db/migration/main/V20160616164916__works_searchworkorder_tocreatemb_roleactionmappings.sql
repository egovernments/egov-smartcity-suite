---------Measurement Book Module--------
insert into EG_MODULE (ID,NAME,ENABLED,CONTEXTROOT,PARENTMODULE,DISPLAYNAME,ORDERNUMBER) VALUES (NEXTVAL('SEQ_EG_MODULE'),'WorksMeasurementBook','true',null,(select id from eg_module where name = 'Works Management'),'Measurement Book', 10); 

--------Actions and Roleactions to search Workorder
insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values(NEXTVAL('SEQ_EG_ACTION'),'SearchWorkOrderForm','/workorder/searchform',
null,(select id from EG_MODULE where name = 'WorksMeasurementBook'),1,'Create Abstract MB',true,'egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));

insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='SearchWorkOrderForm' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Creator'),(select id from eg_action where name ='SearchWorkOrderForm' and contextroot = 'egworks'));

insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values(NEXTVAL('SEQ_EG_ACTION'),'AjaxWorkOrderSearch','/workorder/ajax-search',
null,(select id from EG_MODULE where name = 'WorksMeasurementBook'),1,'Search Work Order',false,'egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));

insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='AjaxWorkOrderSearch' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Creator'),(select id from eg_action where name ='AjaxWorkOrderSearch' and contextroot = 'egworks'));

insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values(NEXTVAL('SEQ_EG_ACTION'),'AjaxWorkOrderNumberSearch','/letterofacceptance/ajaxworkorder-mbheader',
null,(select id from EG_MODULE where name = 'WorksMeasurementBook'),1,'Ajax Search Work Order Number',false,'egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));

insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='AjaxWorkOrderNumberSearch' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Creator'),(select id from eg_action where name ='AjaxWorkOrderNumberSearch' and contextroot = 'egworks'));

--rollback delete from eg_roleaction where actionid in(select id from eg_action where name in('SearchWorkOrderForm','AjaxWorkOrderSearch','AjaxWorkOrderNumberSearch')) and roleid in(select id from eg_role where name in('Super User','Works Creator'));
--rollback delete from eg_action where name in('SearchWorkOrderForm','AjaxWorkOrderSearch','AjaxWorkOrderNumberSearch');
--rollback delete from eg_module where name='WorksMeasurementBook';