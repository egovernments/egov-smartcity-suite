#UP

/****************************************************************************************************/
/**********   Payhead Rule - PctBasic Plus PctHRA     ****************************/

insert into eg_script
   (ID, NAME, SCRIPT_TYPE, SCRIPT, START_DATE, END_DATE)
 Values
   (eg_script_seq.nextVal, 'Payroll.PayHeadRule.PctBasicHRA', 'python','from org.egov.infstr import ValidationError
from org.egov.infstr import ValidationException
from java.lang import Math
from java.math import BigDecimal
def getEarningObjByPayhead(payheadName,currPayslip):
    try:
        for earningObj in currPayslip.getEarningses():
            if earningObj.getSalaryCodes().getHead()==payheadName:
                return (earningObj)
    except ValidationException:
        raise ValidationException,[ValidationError(''Earning is not proper one'',''Earning is not proper one'')]
    return(None)
basicAmt=0.0
hraAmt=0.0
basicCalcAmt=0.0
hraCalcAmt=0.0
result=0.0
validationErrors=None
try:
    basicEarning=getEarningObjByPayhead(''Basic'',currPayslip)
    if basicEarning!=None:
        basicAmt=basicEarning.getAmount().intValue()
    else:
        validationErrors=''Basic not there in payscale''
except ValidationException,e:
    validationErrors=e.getErrors()
try:
    hraEarning=getEarningObjByPayhead(''HRA'',currPayslip)
    if hraEarning != None:
        hraAmt=hraEarning.getAmount().intValue()
    else:
        validationErrors=''HRA not there in payscale''
except ValidationException,e:
    validationErrors=e.getErrors()
print basicAmt
basicCalcAmt=((basicAmt)*(10.0/100.0))
print basicCalcAmt
print hraAmt
hraCalcAmt=((hraAmt)*(20.0/100.0))
print hraCalcAmt
result=BigDecimal.valueOf(Math.round(basicCalcAmt+hraCalcAmt))
print result
result,validationErrors',TO_DATE('01/01/1900 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('01/01/2100 00:00:00', 'MM/DD/YYYY HH24:MI:SS'));


INSERT INTO EG_WF_ACTIONS ( ID, TYPE, NAME, DESCRIPTION, CREATED_BY, CREATED_DATE, MODIFIED_BY,MODIFIED_DATE ) VALUES ( 
EG_WF_ACTIONS_SEQ.NEXTVAL, 'PayHeadRule', 'PctBasicHRA', 'PctBasic Plus PctHRA',NULL , sysdate, NULL, sysdate);

insert into eg_script
   (ID, NAME, SCRIPT_TYPE, SCRIPT, START_DATE, END_DATE)
 Values
   (eg_script_seq.nextVal, 'Payroll.PayHeadRule.PctBasic', 'python', 'from org.egov.infstr import ValidationError          
from org.egov.infstr import ValidationException
from java.lang import Math
from java.math import BigDecimal
basicAmt=0.0
result=0.0
validationErrors=None
try:
    basicEarning =payheadRuleUtil.getEarningByNameFormPayslip(currPayslip,''Basic'')
    if basicEarning!=None:
        basicAmt=basicEarning.getAmount().intValue()
    else:
        validationErrors=''Basic not there in payscale''
except ValidationException,e:
    validationErrors=e.getErrors()  
result=BigDecimal.valueOf(Math.round((basicAmt)*(10.0/100.0)))
print result
result,validationErrors',TO_DATE('01/01/1900 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('01/01/2100 00:00:00', 'MM/DD/YYYY HH24:MI:SS'));

INSERT INTO EG_WF_ACTIONS ( ID, TYPE, NAME, DESCRIPTION, CREATED_BY, CREATED_DATE, MODIFIED_BY,MODIFIED_DATE ) VALUES ( 
EG_WF_ACTIONS_SEQ.NEXTVAL, 'PayHeadRule', 'PctBasic', 'Pct Of Basic',NULL , sysdate, NULL, sysdate);

/*************************************************************************************************************/
/**********    Payhead Rule - Pct Of Basic AttendanceBased Y     ****************************/

insert into eg_script
   (ID, NAME, SCRIPT_TYPE, SCRIPT, START_DATE, END_DATE)
 Values
   (eg_script_seq.nextVal, 'Payroll.PayHeadRule.PctBasicAttendanceY', 'python', 'from org.egov.infstr import ValidationError
from org.egov.infstr import ValidationException
from java.lang import Math
from java.math import BigDecimal
basicAmt=0.0
calculateOnBasic=0.0
calculateOnAttendance=0.0
result=0.0
validationErrors=None
try:
    basicEarning =payheadRuleUtil.getEarningByNameFormPayslip(currPayslip,''Basic'')
    if basicEarning!=None:
        basicAmt=basicEarning.getAmount().intValue()
    else:
        validationErrors=''Basic not there in payscale''
except ValidationException,e:
    validationErrors=e.getErrors()
print basicAmt
calculateOnBasic = (basicAmt)*(10.0/100.0)
print calculateOnBasic
try:
    calculateOnAttendance = payheadRuleUtil.getCalculatedAmountbasedOnAttendance(BigDecimal.valueOf(calculateOnBasic),currPayslip)
except ValidationException,e:
    validationErrors=e.getErrors()
result=Math.round(calculateOnAttendance.doubleValue())
print result
result,validationErrors',TO_DATE('01/01/1900 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('01/01/2100 00:00:00', 'MM/DD/YYYY HH24:MI:SS'));

INSERT INTO EG_WF_ACTIONS ( ID, TYPE, NAME, DESCRIPTION, CREATED_BY, CREATED_DATE, MODIFIED_BY,MODIFIED_DATE ) VALUES ( 
EG_WF_ACTIONS_SEQ.NEXTVAL, 'PayHeadRule', 'PctBasicAttendanceY', 'Pct Of Basic AttendanceBased Y',NULL , sysdate, NULL, sysdate);


/****************************************************************************************************/
/**********   Payhead Rule - DA By Basic Plus Gradepay      ****************************/

insert into eg_script
   (ID, NAME, SCRIPT_TYPE, SCRIPT, START_DATE, END_DATE)
 Values
   (eg_script_seq.nextVal, 'Payroll.PayHeadRule.PctBasicGradePay', 'python', 'from org.egov.infstr import ValidationError          
from org.egov.infstr import ValidationException
basicAmt=0.0
gradePayAmt=0.0
result=0.0
validationErrors=None
try:
    basicEarning =payheadRuleUtil.getEarningByNameFormPayslip(currPayslip,''Basic'')
    if basicEarning!=None:
        basicAmt=basicEarning.getAmount().intValue()
    else:
        validationErrors=''Basic not there in payscale''
except ValidationException,e:
    validationErrors=e.getErrors()  
try:
    gradePayEarning =payheadRuleUtil.getEarningByNameFormPayslip(currPayslip,''GradePay'')
    if gradePayEarning!=None:
        gradePayAmt=gradePayEarning.getAmount().intValue()
    else:
        validationErrors=''Gradepay not there in payscale''
except ValidationException,e:
    validationErrors=e.getErrors()
result=(basicAmt+gradePayAmt)*(20.0/100.0)
print result
result,validationErrors',TO_DATE('01/01/1900 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('01/01/2100 00:00:00', 'MM/DD/YYYY HH24:MI:SS'));

INSERT INTO EG_WF_ACTIONS ( ID, TYPE, NAME, DESCRIPTION, CREATED_BY, CREATED_DATE, MODIFIED_BY,MODIFIED_DATE ) VALUES ( 
EG_WF_ACTIONS_SEQ.NEXTVAL, 'PayHeadRule', 'PctBasicGradePay', 'Pct of Basic Plus Gradepay',NULL , sysdate, NULL, sysdate);

/**********   Payhead Rule - Rule based Ot Pay   ****************************/
   
   Insert into eg_script
   (ID, NAME, SCRIPT_TYPE, SCRIPT, START_DATE, END_DATE)
 Values
   (eg_script_seq.nextVal, 'Payroll.PayHeadRule.OtPayHead', 'python','from org.egov.infstr import ValidationError          
from org.egov.infstr import ValidationException
from java.lang import Math
from java.lang import Integer
from org.egov.pims.empLeave.service import EmpLeaveManagerBean
from java.math import BigDecimal
from org.egov.payroll.services.payslip import PayRollManagerBean
from org.egov.payroll.model import PayStructure
from org.egov.pims.empLeave.model import EmployeeAttendenceReport
from org.egov.payroll.utils import PayrollManagersUtill

otAmt=0.0
noOfOts=0
noOfWorkingDaysInMonth=Integer(0)
noOfPresents=0.0
tmpAmnt=0.0
result=0.0
validationErrors=None
try:
    proll=PayrollManagersUtill()
    employeeAttendenceReport=proll.getEmpLeaveManager().getEmployeeAttendenceReportBetweenTwoDates(currPayslip.getFromDate(),currPayslip.getToDate(),currPayslip.getEmployee())
    noOfWorkingDaysInMonth=employeeAttendenceReport.getDaysInMonth()
    noOfPresents=employeeAttendenceReport.getNoOfPresents()
    noOfOts=employeeAttendenceReport.getNoOfOverTime()
    payStructure= proll.getPayRollManager().getPayStructureForEmpByDate(currPayslip.getEmployee().getIdPersonalInformation(),currPayslip.getToDate())
    if payStructure.getCurrBasicPay()!=None:
        tmpAmnt=payStructure.getCurrBasicPay()
        otAmt=Math.round(tmpAmnt.doubleValue()/noOfWorkingDaysInMonth*noOfOts)
    elif payStructure.getDailyPay()!=None:
         tmpAmnt=payStructure.getDailyPay()
         otAmt= Math.round(tmpAmnt.doubleValue() * noOfOts)
except ValidationException,e:
    validationErrors=e.getErrors()  
result=otAmt
result,validationErrors', TO_DATE('01/01/1900 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('01/01/2100 00:00:00', 'MM/DD/YYYY HH24:MI:SS'));

INSERT INTO EG_WF_ACTIONS ( ID, TYPE, NAME, DESCRIPTION, CREATED_BY, CREATED_DATE, MODIFIED_BY,MODIFIED_DATE ) VALUES ( 
EG_WF_ACTIONS_SEQ.NEXTVAL, 'PayHeadRule', 'OtPayHead', 'Ot pay for overtime', NULL, NULL, NULL, NULL); 

commit;

#DOWN
