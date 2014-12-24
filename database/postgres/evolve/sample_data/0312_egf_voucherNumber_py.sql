#UP
update eg_script set script='from java.text import SimpleDateFormat
from java.math import BigDecimal
from org.egov.commons.service import CommonsManager  
from org.egov.infstr.utils import ServiceLocator  
from org.egov.commons.service import CommonsManagerHome  
from java.lang import Long
from java.lang import Integer
from java.lang import String
from org.egov.commons import EgNumbers
from org.egov.commons import CFiscalPeriod
commonsManagerHome = ServiceLocator.getInstance().getLocalHome(''CommonsManagerHome'')  
commonsManager = commonsManagerHome.create()  
financialYear = commonsManager.getFinancialYearByDate(date)  
year=financialYear.getFinYearRange()
finYearId=BigDecimal(financialYear.getId())
egnum_voucherType=fundIdentity+''/''+voucherType
if (vNumGenMode == ''Auto''): 
	fiscalPeriodid=persistenceService.find("from CFiscalPeriod where financialYearId=?",[finYearId.intValue()])
	bigFiscal=BigDecimal(fiscalPeriodid.getId())
	egnum=persistenceService.find("from EgNumbers where vouchertype=? and fiscialperiodid=?",[egnum_voucherType,bigFiscal])
	if egnum:
		transNumber=egnum.getVouchernumber().add(BigDecimal.ONE)
		egnum.setVouchernumber(transNumber)
	else:
		egnum=EgNumbers()
		egnum.setVouchertype(egnum_voucherType)
		egnum.setVouchernumber(BigDecimal.ONE)
		transNumber=1
		egnum.setFiscialperiodid(bigFiscal)
	persistenceService.setType(EgNumbers)
	persistenceService.getSession().saveOrUpdate(egnum)
	result=egnum_voucherType+''/''+str(transNumber)+''/''+month+''/''+year
else:  
	result=fundIdentity+''/''+voucherType+voucherNumber'   where name='voucherheader.vouchernumber';
#DOWN

