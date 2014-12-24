#UP

update eg_Script set script='from org.egov.infstr import ValidationError
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
        avgBasic=pensionManager.getAverageOfOriginalBasicFromPayslipList(lisOfTenPayslip)
        payCommision=pensionManager.getPayFixedForpayslip(lastPayslip)
        servicePeriod=pensionManager.getServicePeriodForEmployee(employee)
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
result,validationErrors' where name like'PensionCompute';

#DOWN