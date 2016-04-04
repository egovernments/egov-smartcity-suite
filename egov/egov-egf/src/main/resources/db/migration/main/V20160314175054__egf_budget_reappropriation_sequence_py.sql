INSERT INTO EG_SCRIPT (ID,NAME,type,SCRIPT,version) VALUES(nextval('seq_eg_script'),'egf.budget.reappropriation.sequence.generator','python',
'from org.egov.infra.validation.exception import ValidationError  
from org.egov.infstr.utils.seqgen import DatabaseSequenceFirstTimeException   
finRange=wfItem.getBudget().getFinancialYear().getFinYearRange()   
be_sequence="BDGT-REAPPRN-BE-"+finRange   
re_sequence="BDGT-REAPPRN-RE-"+finRange   
sequenceNumber = 0      
type = wfItem.getBudget().getIsbere()  
def getBdgtReAppNum():      
    if type == "RE":       
        try:  
            sequenceNumber = sequenceGenerator.getNextNumberWithFormat(re_sequence,4,"0",0).getFormattedNumber().zfill(4)       
        except DatabaseSequenceFirstTimeException,e:  
            return (None,[ValidationError(e.getMessage(),e.getMessage())])  
    else:       
        try:  
            sequenceNumber = sequenceGenerator.getNextNumberWithFormat(be_sequence,4,"0",0).getFormattedNumber().zfill(4)  
        except DatabaseSequenceFirstTimeException,e:  
            return (None,[ValidationError(e.getMessage(),e.getMessage())])  
    result=type+"/"+finRange+"/"+sequenceNumber  
    return (result,None)  
result,validationErrors=getBdgtReAppNum()',0);

