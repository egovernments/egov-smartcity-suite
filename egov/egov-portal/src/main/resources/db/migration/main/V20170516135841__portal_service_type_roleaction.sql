INSERT into EG_MODULE (ID,NAME,ENABLED,CONTEXTROOT,PARENTMODULE,DISPLAYNAME,ORDERNUMBER) VALUES (NEXTVAL('SEQ_EG_MODULE'),'Portal Services','true','portal',(select id from eg_module where name='Portal Masters'),'Service Type', (select max(ordernumber)+1 from eg_module where contextroot is not null));


INSERT into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'ModifyPortalServiceType','/portalservicetype/search',null,(select id from eg_module where name='Portal Services'),1,'Modify Portal ServiceType','true','portal',0,1,now(),1,now(),(select id from eg_module where name='Citizen Portal' and parentmodule is NULL));

INSERT into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'AjaxSearchServiceNames','/portalservicetype/ajaxboundary-servicesbymodule',null,(select id from eg_module where name='Portal Services'),1,'AjaxSearchServiceNames','false','portal',0,1,now(),1,now(),(select id from eg_module where name='Citizen Portal' and parentmodule is NULL));

INSERT into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'SearchPortalServiceType','/portalservicetype/search','mode=edit',(select id from eg_module where name='Portal Services'),1,'SearchPortalServiceType','false','portal',0,1,now(),1,now(),(select id from eg_module where name='Citizen Portal' and parentmodule is NULL));

INSERT into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'EditServiceForm','/portalservicetype/update/',null,(select id from eg_module where name='Portal Services'),1,'EditServiceForm','false','portal',0,1,now(),1,now(),(select id from eg_module where name='Citizen Portal' and parentmodule is NULL));

INSERT into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'UpdatePortalServiceType','/portalservicetype/update',null,(select id from eg_module where name='Portal Services'),1,'UpdatePortalServiceType','false','portal',0,1,now(),1,now(),(select id from eg_module where name='Citizen Portal' and parentmodule is NULL));


insert into eg_roleaction (ROLEID,ACTIONID) values ((select id from eg_role where name LIKE 'Super User'),(select id FROM eg_action  WHERE name = 'ModifyPortalServiceType'));

insert into eg_roleaction (ROLEID,ACTIONID) values ((select id from eg_role where name LIKE 'Super User'),(select id FROM eg_action  WHERE name = 'AjaxSearchServiceNames'));

insert into eg_roleaction (ROLEID,ACTIONID) values ((select id from eg_role where name LIKE 'Super User'),(select id FROM eg_action  WHERE name = 'SearchPortalServiceType'));

insert into eg_roleaction (ROLEID,ACTIONID) values ((select id from eg_role where name LIKE 'Super User'),(select id FROM eg_action  WHERE name = 'EditServiceForm'));

insert into eg_roleaction (ROLEID,ACTIONID) values ((select id from eg_role where name LIKE 'Super User'),(select id FROM eg_action  WHERE name = 'UpdatePortalServiceType'));
