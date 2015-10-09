update eg_action set contextroot='eis' where id in(select id from eg_action where name='AjaxDesignationsByDepartment');

update eg_action set contextroot='eis' where id in(select id from eg_action where name='AjaxApproverByDesignationAndDepartment');
