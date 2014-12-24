#UP
update eg_script set script='from org.egov.commons.service import CommonsManager
from org.egov.infstr.utils import ServiceLocator
from org.egov.commons.service import CommonsManagerHome
from org.egov.infstr.utils.seqgen import DatabaseSequence
from org.egov.infstr import ValidationError
from java.lang import Long
from java.lang import Integer
from java.lang import String
from org.egov.infstr.utils.seqgen import DatabaseSequenceFirstTimeException
commonsManagerHome = ServiceLocator.getInstance().getLocalHome(''CommonsManagerHome'')
commonsManager = commonsManagerHome.create()
financialYear = commonsManager.getFinancialYearByDate(date)
year=financialYear.getFinYearRange()
transNumber=''''
egnum_voucherType=fundIdentity+''/''+voucherType
def getVoucherNum():  
	if (vNumGenMode == ''Auto''):
		try:
			transNumber = DatabaseSequence.named(sequenceName).createIfNecessary().nextVal()
        	except DatabaseSequenceFirstTimeException,e:
        		return (None,[ValidationError(e.getMessage(),e.getMessage())])
		print transNumber
		print len(str(transNumber))
		for num in range(len(str(transNumber)), 8):
			transNumber="0"+str(transNumber)
		print ''after loop''
		print transNumber
		result=egnum_voucherType+''/''+str(transNumber)+''/''+month+''/''+year
		return (result,None)
	else:
		result=egnum_voucherType+voucherNumber
		return (result,None)
result,validationErrors=getVoucherNum()'
where name = 'voucherheader.vouchernumber' ;

#DOWN

update eg_script set script='
from java.text import SimpleDateFormat  
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
egnum_voucherType=fundIdentity+voucherType  
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
	for num in range(len(str(transNumber)), 8):
		transNumber="0"+str(transNumber)
	result=egnum_voucherType+''/''+str(transNumber)+''/''+month+''/''+year  
else:    
	result=fundIdentity+''/''+voucherType+voucherNumber ' where name='voucherheader.vouchernumber';
