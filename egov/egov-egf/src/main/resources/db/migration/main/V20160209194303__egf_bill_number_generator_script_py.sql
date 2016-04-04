INSERT INTO EG_SCRIPT (ID,NAME,type,SCRIPT,version) VALUES(nextval('seq_eg_script'),'egf.bill.number.generator','python',
'from org.egov.infra.validation.exception import ValidationError
from org.egov.infstr.utils.seqgen import DatabaseSequenceFirstTimeException
def getBillNum():
	try:
		result=sItem.getEgBillregistermis().getEgDepartment().getCode()+"/"+"EJV"+"/"+sequenceGenerator.getNextNumber("EJV",1).getFormattedNumber().zfill(4)+"/"+year
		return (result,None)
	except DatabaseSequenceFirstTimeException,e:
        	return (None,[ValidationError(e.getMessage(),e.getMessage())])
result,validationErrors=getBillNum()',0);
