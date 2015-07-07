INSERT
INTO eg_script
  (
    id,
    name,
    type,
    createdby,
    createddate,
    lastmodifiedby,
    lastmodifieddate,
    script,
    startdate,
    enddate,
    version
  )
  VALUES
  (
    nextval('seq_eg_script'),
    'voucherheader.vouchernumber',
    'python',
    NULL,
    NULL,
    NULL,
    NULL,
    'from org.egov.infstr.utils.seqgen import DatabaseSequence
from org.egov.infstr import ValidationError
from java.lang import Long
from java.lang import Integer
from java.lang import String
from java.lang import Exception
from org.egov.infstr.utils.seqgen import DatabaseSequenceFirstTimeException
from org.egov.infstr.utils import HibernateUtil
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
			transNumber = DatabaseSequence.named(sequenceName,HibernateUtil.getCurrentSession()).createIfNecessary().nextVal()
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
result,validationErrors=getVoucherNum()',
    '1900-01-01 00:00:00',
    '2100-01-01 00:00:00',0);
    

