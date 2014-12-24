#UP

INSERT INTO EG_SCRIPT ( ID, NAME, SCRIPT_TYPE, CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE,SCRIPT, START_DATE, END_DATE ) VALUES ( 
EG_SCRIPT_SEQ.NEXTVAL, 'voucherheader.vouchernumber', 'python', NULL, NULL, NULL, NULL, '
from java.text import SimpleDateFormat
from org.egov.commons.service import CommonsManager
from org.egov.infstr.utils import ServiceLocator
from org.egov.commons.service import CommonsManagerHome
from java.lang import Long
commonsManagerHome = ServiceLocator.getInstance().getLocalHome(''CommonsManagerHome'')
commonsManager = commonsManagerHome.create()
fDate = SimpleDateFormat(''dd/MM/yyyy'').format(date)
financialYearId = commonsManager.getFinancialYearId(fDate)
financialYear= commonsManager.getFinancialYearById(Long.valueOf(financialYearId))
year=financialYear.getFinYearRange()
month=fDate.split(''/'')[1]
if (vNumGenMode == ''Auto''):
	result=fundIdentity+voucherType+"/"+transNumber+"/"+month+"/"+year
else:
	result=fundIdentity+voucherType+voucherNumber'
,  TO_DATE( '01/01/1900 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_DATE( '01/01/2100 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM')); 


#DOWN 