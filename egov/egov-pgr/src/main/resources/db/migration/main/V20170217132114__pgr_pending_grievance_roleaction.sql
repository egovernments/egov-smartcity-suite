insert into eg_roleaction values ((select id from eg_role where name='Grievance Officer'),(select id from eg_action where name='PendingGrievance'));
insert into eg_roleaction values ((select id from eg_role where name='Redressal Officer'),(select id from eg_action where name='PendingGrievance'));
insert into eg_roleaction values ((select id from eg_role where name='Grievance Routing Officer'),(select id from eg_action where name='PendingGrievance'));

insert into eg_roleaction values ((select id from eg_role where name='Grievance Officer'),(select id from eg_action where name='AjaxPendingGrievanceResult'));
insert into eg_roleaction values ((select id from eg_role where name='Redressal Officer'),(select id from eg_action where name='AjaxPendingGrievanceResult'));
insert into eg_roleaction values ((select id from eg_role where name='Grievance Routing Officer'),(select id from eg_action where name='AjaxPendingGrievanceResult'));

