#UP

update eg_script set script='from java.text import SimpleDateFormat
from org.egov.commons.service import CommonsManager
from org.egov.infstr.utils import ServiceLocator
from org.egov.commons.service import CommonsManagerHome
from java.lang import Long
commonsManagerHome = ServiceLocator.getInstance().getLocalHome(''CommonsManagerHome'')
commonsManager = commonsManagerHome.create()
financialYearId = commonsManager.getFinancialYearByDate(date)
financialYear= commonsManager.getFinancialYearById(Long.valueOf(financialYearId.getId()))
year=financialYear.getFinYearRange()
if (vNumGenMode == ''Auto''):
	result=fundIdentity+voucherType+"/"+transNumber+"/"+month+"/"+year
else:
	result=fundIdentity+voucherType+voucherNumber' 
where name='voucherheader.vouchernumber';


#DOWN
