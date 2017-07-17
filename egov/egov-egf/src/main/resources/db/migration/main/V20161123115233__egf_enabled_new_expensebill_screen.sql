update eg_action set enabled = true where name = 'CreateExpenseBillNewForm';

update eg_wf_types set link = '/EGF/expensebill/update/:ID' where type = 'EgBillregister';
