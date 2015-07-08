UPDATE eg_script SET script ='from org.egov.infstr import ValidationError
from java.lang import Long
from java.lang import Integer
from java.lang import String
from java.lang import Exception
from org.hibernate.exception import SQLGrammarException
transNumber=''''
egnum_voucherType=fundIdentity+''/''+voucherType
def getVoucherNum():  
	if (vNumGenMode == ''Auto''):
		try:
			financialYear = commonsService.getFinancialYearByDate(date)
		except Exception,e:
			return (None,[ValidationError(''Financial Year is not active for posting.'',''Financial Year is not active for posting.'')])
		year=financialYear.getFinYearRange()
		try:
			transNumber = sequenceNumberGenerator.getNextSequence(sequenceName)
        	except SQLGrammarException,e:
			transNumber = dbSequenceGenerator.createAndGetNextSequence(sequenceName)
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
result,validationErrors=getVoucherNum()' where name = 'voucherheader.vouchernumber';
