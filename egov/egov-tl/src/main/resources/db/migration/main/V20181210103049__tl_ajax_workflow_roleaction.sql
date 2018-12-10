insert into eg_roleaction values ((select id from eg_role where name='TLApprover'),(select id from eg_action where name='findDesignations-byDepartment' and contextroot='eis'));

delete from eg_feature_action where action=(select id from eg_action where name = 'AjaxDesignationDropdown') and feature in (select id from eg_feature where name in ('Closure Application Approval','Renew License','Create New License','License Approval'));

delete from eg_feature_action where action=(select id from eg_action where name='AjaxDesignationsByDepartmentWithDesignation') and feature in (select id from eg_feature where name in ('Closure Application Approval'));

insert into eg_feature_action values ((select id from eg_action where name='findDesignations-byDepartment'),(select id from eg_feature where name ='License Approval'));

insert into eg_feature_action  values ((select id from eg_action where name = 'FindDesignationsByDepartmentWithDesignation' and contextroot='eis'),(select id from eg_feature where name ='Closure Application Approval'));