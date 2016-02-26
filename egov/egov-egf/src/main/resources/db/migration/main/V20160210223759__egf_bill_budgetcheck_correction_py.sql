UPDATE eg_script SET script ='from  org.egov.infra.validation.exception import ValidationException
from org.egov.infra.validation.exception import ValidationError
try:
	result=voucherService.budgetaryCheck(bill)
except ValidationException,e:
    validationErrors=e.getErrors()
    result=None' where name = 'egf.bill.budgetcheck';
