INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Sewerage Tax Approver'), (select id from eg_action where name = 'AjaxDesignationsByDepartment' and contextroot = 'eis')); 

INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Sewerage Tax Approver'), (select id from eg_action where name = 'AjaxApproverByDesignationAndDepartment' and contextroot = 'eis')); 

update eg_wf_matrix set nextstate='Assistant Engineer Approved' where currentstate='Inspection Fee Collected' and objecttype='SewerageApplicationDetails' and additionalrule='NEWSEWERAGECONNECTION';