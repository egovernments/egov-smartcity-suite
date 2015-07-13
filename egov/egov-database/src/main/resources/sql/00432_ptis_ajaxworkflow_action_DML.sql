update eg_action  set contextroot='ptis' where name='AjaxDesignationDropdown';

update eg_action  set contextroot='ptis' where name='AjaxApproverDropdown';

--rollback update eg_action  set contextroot='eis' where name='AjaxApproverDropdown';
--rollback update eg_action  set contextroot='eis' where name='AjaxDesignationDropdown';
