Insert into eg_action (id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,createddate,createdby,lastmodifieddate,lastmodifiedby,application,version) 
values(nextval('SEQ_EG_ACTION'),'Edit Employee Contact','/employee/search/updatecontact',(select id from eg_module where name='Employee'),
4,'Update Contact Detail',true,'eis',now(),1,now(),1,(select id from eg_module where name='EIS' and parentmodule is null),0);

insert into eg_roleaction values((select id from eg_role where name='Employee Admin'),(select id from eg_action where name='Edit Employee Contact' and contextroot = 'eis'));
insert into eg_roleaction values((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='Edit Employee Contact' and contextroot = 'eis'));
