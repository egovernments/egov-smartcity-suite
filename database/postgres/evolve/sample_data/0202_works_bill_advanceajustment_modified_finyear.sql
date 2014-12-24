#UP
update transactionsummary
set FINANCIALYEARID=(select id from financialyear where financialyear='2010-11')
where
GLCODEID=(select id from chartofaccounts coa where coa.glcode='4604002' and coa.name='Contractors')
and ACCOUNTDETAILTYPEID=(select id from accountdetailtype where name='contractor')
and ACCOUNTDETAILKEY=(select id from accountdetailkey where detailkey=(select id from egw_contractor where code='TestCodeWorks123') and  DETAILNAME='contractor_id')
and FINANCIALYEARID=(select id from financialyear where financialyear='2009-10');

update VOUCHERHEADER
set VOUCHERDATE=to_timestamp('01-04-10','DD-MM-RR HH12:MI:SSXFF AM')
where VOUCHERNUMBER='CJ00001224';
#DOWN


