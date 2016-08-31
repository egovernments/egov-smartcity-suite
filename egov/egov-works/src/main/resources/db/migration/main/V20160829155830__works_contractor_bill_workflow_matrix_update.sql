-----------Update script to show approver designation in contractor bill screen---------------
update EG_WF_MATRIX set nextdesignation = 'Assistant engineer,Assistant executive engineer'  where id = (select id from eg_wf_matrix where currentstate = 'Rejected' and objecttype='ContractorBillRegister');
update EG_WF_MATRIX set nextdesignation = 'Assistant engineer,Assistant executive engineer'  where id = (select id from eg_wf_matrix where currentstate = 'NEW' and objecttype='ContractorBillRegister');

--rollback update EG_WF_MATRIX set nextdesignation = 'Assistant engineer'  where id = (select id from eg_wf_matrix where currentstate = 'Rejected' and objecttype='ContractorBillRegister');
--rollback update EG_WF_MATRIX set nextdesignation = 'Assistant engineer'  where id = (select id from eg_wf_matrix where currentstate = 'NEW' and objecttype='ContractorBillRegister');