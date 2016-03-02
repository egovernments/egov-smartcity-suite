delete from eg_roleaction where actionid in (Select id from eg_action where url like '%DayBook.jsp%');
delete from eg_action where url like '%DayBook.jsp%';

delete from eg_roleaction where actionid in (Select id from eg_action where url like '%JournalBook.jsp%');
delete from eg_action where url like '%JournalBook.jsp%';

delete from eg_roleaction where actionid in (Select id from eg_action where url like '%GeneralLedger.jsp%');
delete from eg_action where url like '%GeneralLedger.jsp%';


delete from eg_roleaction where actionid in (Select id from eg_action where url like '%/deduction/remitRecovery.do%');
delete from eg_action where url like '%/deduction/remitRecovery.do%';


delete from eg_roleaction where actionid in (Select id from eg_action where url like '%recoverySetupMaster.do%');
delete from eg_action where url like '%recoverySetupMaster.do%';
 
