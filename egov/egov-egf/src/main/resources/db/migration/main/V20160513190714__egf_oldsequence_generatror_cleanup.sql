
update eg_script set script='financialYear = commonsService.getFinancialYearByDate(bill.getBilldate())
year=financialYear.getFinYearRange()
result=bill.getEgBillregistermis().getEgDepartment().getCode()+"/"+"MN"+"/"+sequenceGenerator.getNextSequence("sq_billnumber_mn").toString().zfill(4)+"/"+year' where name='autobillnumber';

create sequence sq_billnumber_mn;

SELECT setval('sq_billnumber_mn', (Select case when value = null then 1 else case when value=0 then 1 else value end end from eg_number_generic where objecttype='MN'));

update eg_script set script='be_sequence="BUDGET_REAPPROPRIATION" 
result = "BANo:"+sequenceGenerator.getNextSequence(be_sequence).toString().zfill(3)+"/"+wfItem.getFinYearRange()' where name='egf.reappropriation.sequence.generator';


create sequence sq_BUDGET_REAPPROPRIATION;

SELECT setval('sq_BUDGET_REAPPROPRIATION', (Select case when value = null then 1 else case when value=0 then 1 else value end end from eg_number_generic where objecttype='BUDGET-REAPPROPRIATION'));



update eg_script set script='from org.egov.infra.validation.exception import ValidationError  
from org.egov.infstr.utils.seqgen import DatabaseSequenceFirstTimeException   
finRange=wfItem.getBudget().getFinancialYear().getFinYearRange()   
be_sequence="BDGT_REAPPRN_BE_"+finRange   
re_sequence="BDGT_REAPPRN_RE_"+finRange   
sequenceNumber = 0      
type = wfItem.getBudget().getIsbere()  
def getBdgtReAppNum():      
    if type == "RE":       
        try:  
            sequenceNumber = sequenceGenerator.getNextSequence(re_sequence).toString().zfill(4)       
        except DatabaseSequenceFirstTimeException,e:  
            return (None,[ValidationError(e.getMessage(),e.getMessage())])  
    else:       
        try:  
            sequenceNumber = sequenceGenerator.getNextSequence(be_sequence).toString().zfill(4)  
        except DatabaseSequenceFirstTimeException,e:  
            return (None,[ValidationError(e.getMessage(),e.getMessage())])  
    result=type+"/"+finRange+"/"+sequenceNumber  
    return (result,None)  
result,validationErrors=getBdgtReAppNum()' where name='egf.budget.reappropriation.sequence.generator';



update eg_script set script='from org.egov.infra.validation.exception import ValidationError
from org.egov.infstr.utils.seqgen import DatabaseSequenceFirstTimeException
def getBillNum():
	try:
		result=sItem.getEgBillregistermis().getEgDepartment().getCode()+"/"+"EJV"+"/"+sequenceGenerator.getNextSequence("SQ_Bill_EJV").toString().zfill(4)+"/"+year
		return (result,None)
	except DatabaseSequenceFirstTimeException,e:
        	return (None,[ValidationError(e.getMessage(),e.getMessage())])
result,validationErrors=getBillNum()' where name='egf.bill.number.generator';


create sequence SQ_Bill_EJV;

SELECT setval('SQ_Bill_EJV', (Select case when value = null then 1 else case when value=0 then 1 else value end end from eg_number_generic where objecttype='EJV'));
