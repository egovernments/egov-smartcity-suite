#UP


update eg_script set script ='from org.egov.infstr import ValidationError  
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
    if  grossSumAmt <=9999:
        slabAmt=0.0  
    elif  grossSumAmt >=10000 and grossSumAmt <=  14999:
        slabAmt+=150.0
    elif  grossSumAmt >=15000: 
        slabAmt+=200.0  
   
except ValidationException,e:  
    validationErrors=e.getErrors()

result = slabAmt
result,validationErrors' where name='Payroll.PayHeadRule.PTRule';


#DOWN
