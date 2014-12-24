#UP

Insert into eg_script
   (ID, NAME, SCRIPT_TYPE, SCRIPT, START_DATE, END_DATE)
 Values
   (eg_script_seq.nextVal, 'Payroll.PayHeadRule.PTRule', 'python','from org.egov.infstr import ValidationError  
from org.egov.infstr import ValidationException  
from java.lang import Math  
from java.math import BigDecimal
from org.egov.payroll.workflow.payslip import  PayslipService 
from org.egov.payroll.model import EmpPayroll
from java.util import Calendar
def getSumOfGrossAmt(currPayslip):
    try:
         grossAmt = 0.0
         print currPayslip.getGrossPay().intValue()
         grossAmt+=currPayslip.getGrossPay().intValue()
         return (grossAmt) 
    except ValidationException:  
        raise ValidationException,[ValidatiError(''Deduction is not proper one'',''Deduction is not proper one'')] 
    return None
slabAmt=0.0
grossSumAmt =0.0
result=0.0
validationErrors=None
try:
    grossSumAmt=getSumOfGrossAmt(currPayslip)
    print grossSumAmt
    if  grossSumAmt <500:
        slabAmt=0.0  
    elif  grossSumAmt >=501 and grossSumAmt <=  7000:
        slabAmt+=50.0
    elif  grossSumAmt >=7001  and grossSumAmt <=   8000: 
        slabAmt+=100.0  
    elif  grossSumAmt >=8001  and grossSumAmt <= 9000:  
        slabAmt+=120.0
    elif  grossSumAmt >=9001   and grossSumAmt <=   10000:  
        slabAmt+=150.0
    elif grossSumAmt > 10001:
        slabAmt+=200.0
except ValidationException,e:  
    validationErrors=e.getErrors()

result = slabAmt
result,validationErrors', TO_DATE('01/01/1900 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('01/01/2100 00:00:00', 'MM/DD/YYYY HH24:MI:SS'));

#DOWN
