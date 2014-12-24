#UP

Insert into eg_script
   (ID, NAME, SCRIPT_TYPE, SCRIPT, START_DATE, END_DATE)
 Values
   (eg_script_seq.nextVal, 'PensionCompute', 'python', 'from org.egov.infstr import ValidationError
from org.egov.infstr import ValidationException
from org.egov.payroll.utils import PayrollManagersUtill
from org.egov.payroll.dao import PayrollDAOFactory
from java.math import BigDecimal
from org.egov import EGOVException
result=0
validationErrors=None
try:
    try:
        lastPayslip=PayrollManagersUtill.getPayRollManager().getLatestPayslipByEmp(employee)
        lisOfTenPayslip=PayrollDAOFactory.getDAOFactory().getPensionDetailsDAO().getLastTenPayslipByEmployee(employee)
        avgBasic=PayrollManagersUtill.getPensionManager().getAverageOfOriginalBasicFromPayslipList(lisOfTenPayslip)
        payCommision=PayrollManagersUtill.getPensionManager().getPayFixedForpayslip(lastPayslip)
        servicePeriod=PayrollManagersUtill.getPensionManager().getServicePeriodForEmployee(employee)
        if payCommision.getName()==''sixPay'':
            pensionDetailsObj.setDaPercent(BigDecimal.valueOf(22))
        else:
            pensionDetailsObj.setDaPercent(BigDecimal.valueOf(96))
        daComponent = avgBasic.multiply(pensionDetailsObj.getDaPercent()).divide(BigDecimal.valueOf(100))
        pensionDetailsObj.setBasicPay(avgBasic)
        result = avgBasic.add(daComponent).multiply(servicePeriod).divide(BigDecimal.valueOf(66),BigDecimal.ROUND_UP)
        pensionDetailsObj.setMonthlyPensionAmount(avgBasic.add(daComponent))
    except EGOVException,e:
        raise ValidationException,[ValidationError(''Some Exception in script'',e.getMessage())]
except ValidationException,e:
    validationErrors=e.getErrors()
result,validationErrors', TO_DATE('01/01/1900 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('01/01/2100 00:00:00', 'MM/DD/YYYY HH24:MI:SS'));

#DOWN