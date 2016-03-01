UPDATE eg_script SET script ='from  org.egov.infra.validation.exception import ValidationException
from org.egov.infra.validation.exception import ValidationError
def budgetaryCheck(): 
    try:
	voucherService.budgetaryCheck(bill)
    except ValidationException,e:
         return (None,[ValidationError(e.getErrors().get(0).getMessage(),e.getErrors().get(0).getMessage())])
result,validationErrors=budgetaryCheck()' where name = 'egf.bill.budgetcheck';  
