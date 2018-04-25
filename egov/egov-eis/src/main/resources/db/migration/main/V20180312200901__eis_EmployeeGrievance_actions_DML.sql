
insert into eg_module values (nextval('seq_eg_module'),'Grievance',true,'eis',(Select id from eg_module where name='EIS'),
'Grievance',3);

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),
'New-EmployeeGrievance','/employeegrievance/new',(select id from eg_module where name='Grievance' and
parentmodule=(select id from eg_module where name='EIS' and parentmodule is null)),1,'Create Employee Grievance',true,'eis',
(select id from eg_module where name='EIS' and parentmodule is null));

Insert into eg_roleaction values((select id from eg_role where name='SYSTEM'),(select id from eg_action where name='New-EmployeeGrievance'));
Insert into eg_roleaction values((select id from eg_role where name='EMPLOYEE'),(select id from eg_action where name='New-EmployeeGrievance'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Create-EmployeeGrievance','/employeegrievance/create',(select id from eg_module where name='Grievance' and parentmodule=(select id from eg_module where name='EIS' and parentmodule is null)),1,'Create-EmployeeGrievance',false,'eis',(select id from eg_module where name='EIS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='SYSTEM'),(select id from eg_action where name='Create-EmployeeGrievance'));
Insert into eg_roleaction values((select id from eg_role where name='EMPLOYEE'),(select id from eg_action where name='Create-EmployeeGrievance'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Update-EmployeeGrievance','/employeegrievance/update',(select id from eg_module where name='Grievance' and parentmodule=(select id from eg_module where name='EIS' and parentmodule is null)),1,'Update-EmployeeGrievance',false,'eis',(select id from eg_module where name='EIS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='SYSTEM'),(select id from eg_action where name='Update-EmployeeGrievance'));
Insert into eg_roleaction values((select id from eg_role where name='EMPLOYEE'),(select id from eg_action where name='Update-EmployeeGrievance'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'View-EmployeeGrievance','/employeegrievance/view',(select id from eg_module where name='Grievance' and parentmodule=(select id from eg_module where name='EIS' and parentmodule is null)),1,'View-EmployeeGrievance',false,'eis',(select id from eg_module where name='EIS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='SYSTEM'),(select id from eg_action where name='View-EmployeeGrievance'));
Insert into eg_roleaction values((select id from eg_role where name='EMPLOYEE'),(select id from eg_action where name='View-EmployeeGrievance'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Edit-EmployeeGrievance','/employeegrievance/edit/',(select id from eg_module where name='Grievance' and parentmodule=(select id from eg_module where name='EIS' and parentmodule is null)),1,'Edit-EmployeeGrievance',false,'eis',(select id from eg_module where name='EIS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='SYSTEM'),(select id from eg_action where name='Edit-EmployeeGrievance'));
Insert into eg_roleaction values((select id from eg_role where name='EMPLOYEE'),(select id from eg_action where name='Edit-EmployeeGrievance'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Result-EmployeeGrievance','/employeegrievance/result',(select id from eg_module where name='Grievance' and parentmodule=(select id from eg_module where name='EIS' and parentmodule is null)),1,'Result-EmployeeGrievance',false,'eis',(select id from eg_module where name='EIS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='SYSTEM'),(select id from eg_action where name='Result-EmployeeGrievance'));
Insert into eg_roleaction values((select id from eg_role where name='EMPLOYEE'),(select id from eg_action where name='Result-EmployeeGrievance'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'View EmployeeGrievance','/employeegrievance/search/view',(select id from eg_module where name='Grievance' and parentmodule=(select id from eg_module where name='EIS' and parentmodule is null)),2,'View EmployeeGrievance',true,'eis',(select id from eg_module where name='EIS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='SYSTEM'),(select id from eg_action where name='View EmployeeGrievance'));
Insert into eg_roleaction values((select id from eg_role where name='EMPLOYEE'),(select id from eg_action where name='View EmployeeGrievance'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Edit EmployeeGrievance','/employeegrievance/search/edit',(select id from eg_module where name='Grievance' and parentmodule=(select id from eg_module where name='EIS' and parentmodule is null)),3,'Edit EmployeeGrievance',true,'eis',(select id from eg_module where name='EIS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='SYSTEM'),(select id from eg_action where name='Edit EmployeeGrievance'));
Insert into eg_roleaction values((select id from eg_role where name='EMPLOYEE'),(select id from eg_action where name='Edit EmployeeGrievance'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and View Result-EmployeeGrievance','/employeegrievance/ajaxsearch/view',(select id from eg_module where name='Grievance' and parentmodule=(select id from eg_module where name='EIS' and parentmodule is null)),1,'Search and View Result-EmployeeGrievance',false,'eis',(select id from eg_module where name='EIS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='SYSTEM'),(select id from eg_action where name='Search and View Result-EmployeeGrievance'));
Insert into eg_roleaction values((select id from eg_role where name='EMPLOYEE'),(select id from eg_action where name='Search and View Result-EmployeeGrievance'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and Edit Result-EmployeeGrievance','/employeegrievance/ajaxsearch/edit',(select id from eg_module where name='Grievance' and parentmodule=(select id from eg_module where name='EIS' and parentmodule is null)),1,'Search and Edit Result-EmployeeGrievance',false,'eis',(select id from eg_module where name='EIS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='SYSTEM'),(select id from eg_action where name='Search and Edit Result-EmployeeGrievance'));
Insert into eg_roleaction values((select id from eg_role where name='EMPLOYEE'),(select id from eg_action where name='Search and Edit Result-EmployeeGrievance'));


Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'download-employeeGrievance','/employeegrievance/downloadfile/',(select id from eg_module where name='Grievance' and parentmodule=(select id from eg_module where name='EIS' and parentmodule is null)),0,null,false,'eis',(select id from eg_module where name='EIS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='SYSTEM'),(select id from eg_action where name='download-employeeGrievance'));
Insert into eg_roleaction values((select id from eg_role where name='EMPLOYEE'),(select id from eg_action where name='download-employeeGrievance'));

