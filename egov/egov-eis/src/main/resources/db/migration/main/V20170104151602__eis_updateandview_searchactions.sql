

Insert into eg_action (id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application,createddate,createdby,lastmodifieddate,lastmodifiedby,version) values(nextval('seq_eg_action'),
'Edit Employee Search','/employee/search/update',(select id from eg_module where name='Employee'),1,'Update Employee',true,'eis',(select id from eg_module where name='EIS' and parentmodule is null),current_date,1,current_date,1,0);

Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Edit Employee Search'));
Insert into eg_roleaction values((select id from eg_role where name='Employee Admin'),(select id from eg_action where name='Edit Employee Search'));

update eg_action set url='/employee/search/view',displayname='View Employee' where name='Search Employee';
update eg_Action set url='/employee/search/ajaxemployees' where name='EmpSearchAjax';
