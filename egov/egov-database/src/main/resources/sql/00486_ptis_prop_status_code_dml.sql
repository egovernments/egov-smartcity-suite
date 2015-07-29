update egpt_mutation_master set code = 'ADD_OR_ALTER' where mutation_name = 'ADDITION OR ALTERATION';
update egpt_status set code = 'ADD_OR_ALTER', status_name = 'Alter' where status_name = 'Modify';

--rollback update egpt_status set code = 'MODIFY', status_name = 'Modify' where status_name = 'Alter';
--rollback update egpt_mutation_master set code = 'ADD OR ALTER' where mutation_name = 'ADDITION OR ALTERATION';