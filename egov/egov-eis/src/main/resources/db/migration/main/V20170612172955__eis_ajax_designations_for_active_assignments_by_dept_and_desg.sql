Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'AjaxDesignationsForActiveAssignmentsByDepartment','/ajaxWorkFlow-getDesignationsForActiveAssignmentsByObjectType',null,(select id from eg_module where name='EIS-COMMON'),1,'Approver Designations by Department','false','eis',0,1,now(),1,now(),(select id from eg_module where name = 'EIS'));


insert into eg_roleaction values((select id from eg_role where name='SYSTEM'),(select id from eg_action where name='AjaxDesignationsForActiveAssignmentsByDepartment'));
insert into eg_roleaction values((select id from eg_role where name='Bill Creator'),(select id from eg_action where name='AjaxDesignationsForActiveAssignmentsByDepartment'));
insert into eg_roleaction values((select id from eg_role where name='Bill Approver'),(select id from eg_action where name='AjaxDesignationsForActiveAssignmentsByDepartment'));


Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'AjaxDesignationsForActiveAssignmentsByObjectTypeAndDesignation','/ajaxWorkFlow-getDesignationsForActiveAssignmentsByObjectTypeAndDesignation',null,(select id from eg_module where name='EIS-COMMON'),1,'Approver Designations by Department And Designation','false','eis',0,1,now(),1,now(),(select id from eg_module where name = 'EIS'));


insert into eg_roleaction values((select id from eg_role where name='SYSTEM'),(select id from eg_action where name='AjaxDesignationsForActiveAssignmentsByObjectTypeAndDesignation'));
insert into eg_roleaction values((select id from eg_role where name='Bill Creator'),(select id from eg_action where name='AjaxDesignationsForActiveAssignmentsByObjectTypeAndDesignation'));
insert into eg_roleaction values((select id from eg_role where name='Bill Approver'),(select id from eg_action where name='AjaxDesignationsForActiveAssignmentsByObjectTypeAndDesignation'));
