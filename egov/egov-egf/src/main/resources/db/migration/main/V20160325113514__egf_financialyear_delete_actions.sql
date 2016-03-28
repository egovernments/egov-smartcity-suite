delete from eg_roleaction where actionid in (select id from eg_action where name in ('Search and Edit Result-CFinancialYear','Search and View Result-CFinancialYear','Search and Edit-CFinancialYear','Search and View-CFinancialYear','Result-CFinancialYear','Edit-CFinancialYear','View-CFinancialYear','Update-CFinancialYear','Create-CFinancialYear','New-CFinancialYear'));

delete from eg_action where name in ('Search and Edit Result-CFinancialYear','Search and View Result-CFinancialYear','Search and Edit-CFinancialYear','Search and View-CFinancialYear','Result-CFinancialYear','Edit-CFinancialYear','View-CFinancialYear','Update-CFinancialYear','Create-CFinancialYear','New-CFinancialYear');

delete from eg_module where name = 'Financial Masters Financial year';


