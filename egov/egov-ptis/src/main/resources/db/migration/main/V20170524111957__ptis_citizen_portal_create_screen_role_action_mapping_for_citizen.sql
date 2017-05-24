insert into eg_roleaction (actionid,roleid) values((select id from eg_action where url ='/create/createProperty-newForm.action'),(select id from eg_role where name='Citizen'))

insert into eg_roleaction (actionid,roleid) values((select id from eg_action where url ='/workflow/ajaxWorkFlow-getDesignationsByObjectType.action'),(select id from eg_role where name='Citizen'));

insert into eg_roleaction (actionid,roleid) values((select id from eg_action where url ='/common/ajaxCommon-propTypeCategoryByPropType.action'),(select id from eg_role where name='Citizen'));

insert into eg_roleaction (actionid,roleid) values((select id from eg_action where url ='/common/ajaxcommon-propdepartment-byproptype'),(select id from eg_role where name='Citizen'));

insert into eg_roleaction (actionid,roleid) values((select id from eg_action where url ='/common/ajaxCommon-usageByPropType.action'),(select id from eg_role where name='Citizen'))

insert into eg_roleaction (actionid,roleid) values((select id from eg_action where url ='/ptis/common/ajaxCommon-isAppurTenant.action'),(select id from eg_role where name='Citizen'));

insert into eg_roleaction (actionid,roleid) values((select id from eg_action where url ='/ptis/common/ajaxCommon-getUserByMobileNo.action'),(select id from eg_role where name='Citizen'))

insert into eg_roleaction (actionid,roleid) values((select id from eg_action where url ='/create/createProperty-create.action'),(select id from eg_role where name='Citizen'));

insert into eg_roleaction (actionid,roleid) values((select id from eg_action where url ='/create/createProperty-createDataEntry.action'),(select id from eg_role where name='Citizen'));

insert into eg_roleaction (actionid,roleid) values((select id from eg_action where url ='/create/createProperty-view.action'),(select id from eg_role where name='Citizen'));
