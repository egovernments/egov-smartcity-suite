update egpt_document_type set name='Electricity Bill',transactiontype='VRMONTHLYUPDATE' where name='Electicity Bill' and transactiontype='VACANCYREMISSION';

insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'VacancyRemission GenerateNotice' and contextroot='ptis'), id from eg_role where name='ULB Operator';

