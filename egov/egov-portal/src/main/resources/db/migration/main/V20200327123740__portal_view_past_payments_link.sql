
----view past payments module and link in citizen portal
insert into eg_module(id,name,enabled,contextroot,parentmodule,displayname,ordernumber,rootmodule) values (nextval('seq_eg_module'),'View Past Payments',true,'portal',null,'View Past Payments',34,null);


insert into EGP_PORTALSERVICE (id,module,code,sla,version,url,isactive,name,userservice,businessuserservice,helpdoclink,
createdby,createddate,lastmodifieddate,lastmodifiedby) values(nextval('seq_egp_portalservice'),
(select id from eg_module where name='View Past Payments'),
'View Past Payments',null,0,'/portal/pastpayments/view',true,'View Past Payments',true,true,'/portal/pastpayments/view',
(select id from eg_user where lower(username) ='system' and type='SYSTEM'),now(),now(),(select id from eg_user where lower(username) ='system' and type='SYSTEM'));







