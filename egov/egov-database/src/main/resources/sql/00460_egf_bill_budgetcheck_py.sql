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
    'egf.bill.budgetcheck',
    'python',
    NULL,
    NULL,
    NULL,
    NULL,
    'from  org.egov.infstr import ValidationException
from org.egov.infstr import ValidationError
try:
	result=voucherService.budgetaryCheck(bill)
except ValidationException,e:
    validationErrors=e.getErrors()
    result=None',
    '1900-01-01 00:00:00',
    '2100-01-01 00:00:00',0);
