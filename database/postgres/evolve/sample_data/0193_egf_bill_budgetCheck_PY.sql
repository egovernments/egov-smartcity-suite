
#UP


INSERT INTO EG_SCRIPT (ID,NAME,SCRIPT_TYPE,SCRIPT) VALUES(EG_SCRIPT_SEQ.NEXTVAL,'egf.bill.budgetcheck','python',
'
from  org.egov.infstr import ValidationException
from org.egov.infstr import ValidationError
try:
	result=voucherService.budgetaryCheck(bill)
except ValidationException,e:
    validationErrors=e.getErrors()
    result=None');
#DOWN
delete from eg_script where  name ='egf.bill.budgetcheck';
