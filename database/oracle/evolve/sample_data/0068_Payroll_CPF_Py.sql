#UP

INSERT INTO EG_SCRIPT
   (ID, NAME, SCRIPT_TYPE, SCRIPT, START_DATE, END_DATE)
 Values
   (eg_script_seq.nextVal, 'Payroll.CPFHeader.PctBasic', 'python','from org.egov.infstr import ValidationError          
from org.egov.infstr import ValidationException
from java.lang import Math
from java.math import BigDecimal
totBasicAmt=0.0
totCPFDedAmt=0.0
basicAmt=0.0
cpfDedAmt=0.0
result=0.0
validationErrors=None
try:
    for empPayrollObj in empPayrollList:
    	cpfDedcution=cpfDelegate.getDeductionByNameFromPayslip(empPayrollObj,''CPF'')	
    	basicEarning =cpfDelegate.getEarningByNameFromPayslip(empPayrollObj,''Basic'')
    	if basicEarning!=None :
        	if cpfDedcution!=None:
        		totBasicAmt=totBasicAmt+basicEarning.getAmount().intValue()
    	else:
        	validationErrors=''Basic not there in payscale''
        if cpfDedcution!=None:
        	totCPFDedAmt=totCPFDedAmt+cpfDedcution.getAmount().intValue()
except ValidationException,e:
    validationErrors=e.getErrors()  
basicAmt=Math.round((totBasicAmt)*(10.0/100.0))
cpfDedAmt=totCPFDedAmt
if basicAmt<cpfDedAmt:
	result=basicAmt
else:
	result=cpfDedAmt
print result
result,validationErrors',TO_DATE('01/01/2000 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('01/01/2100 00:00:00', 'MM/DD/YYYY HH24:MI:SS'));


INSERT INTO EG_WF_ACTIONS ( ID, TYPE, NAME, DESCRIPTION, CREATED_BY, CREATED_DATE, MODIFIED_BY,MODIFIED_DATE ) VALUES ( 
EG_WF_ACTIONS_SEQ.NEXTVAL, 'CPFHeader', 'PctBasic', 'Pct Of Basic',NULL , sysdate, NULL, sysdate);


commit;

#DOWN