#UP

/*** Script for payscale, increment by basic plus gradepay.****/
Insert into eg_script
   (ID, NAME, SCRIPT_TYPE, SCRIPT, START_DATE, END_DATE)
 Values
   (eg_script_seq.nextVal, 'Payroll.Payscale.IncrementBasicGradepay', 'python','from org.egov.infstr import ValidationError          
from org.egov.infstr import ValidationException
def getPayscaleDetailByPayhead(payheadName,paystrObj):    
    try:
         for payscaleDetailObj in paystrObj.getPayHeader().getPayscaleDetailses():
             if payscaleDetailObj.getSalaryCodes().getHead()==payheadName:
                    return (payscaleDetailObj)
    except Exception,e:
            raise ValidationException,[ValidationError(''Paystructure is not proper one'',''Paystructure is not proper one'')]    
    return(None)        
basicAmnt=0.0
gradepayAmnt=0.0
result=0.0
validationErrors=None
if paystrObj.getCurrBasicPay()!=None:
    basicAmnt=paystrObj.getCurrBasicPay().intValue()
elif paystrObj.getDailyPay()!=None:
    basicAmnt=paystrObj.getDailyPay().intValue()
try:
    payscaleDetail=getPayscaleDetailByPayhead(''GradePay'',paystrObj)
    if payscaleDetail != None:
        if payscaleDetail.getSalaryCodes().getCalType()==''MonthlyFlatRate'':
            gradepayAmnt=payscaleDetail.getAmount().intValue()
    else:
        validationErrors=''GradePay not there in payscale''
except ValidationException,e:
    validationErrors=e.getErrors()     
result=((basicAmnt+gradepayAmnt)*(20.0/100.0))
result,validationErrors', TO_DATE('01/01/1900 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('01/01/2100 00:00:00', 'MM/DD/YYYY HH24:MI:SS'));

INSERT INTO EG_WF_ACTIONS ( ID, TYPE, NAME, DESCRIPTION, CREATED_BY, CREATED_DATE, MODIFIED_BY,MODIFIED_DATE ) VALUES ( 
EG_WF_ACTIONS_SEQ.NEXTVAL, 'PayScaleHeader', 'IncrementBasicGradepay', 'Increment Basic Plus Gradepay', NULL, NULL, NULL, NULL); 


/*** Script for payscale, increment by basic plus gradepay considering probation.****/

Insert into eg_script
   (ID, NAME, SCRIPT_TYPE, SCRIPT, START_DATE, END_DATE)
 Values
   (eg_script_seq.nextVal, 'Payroll.Payscale.IncrementBasicGradepayForProbation', 'python','from org.egov.infstr import ValidationError            
from org.egov.infstr import ValidationException  
from java.lang import Integer 
from org.egov.payroll.rules import PayscaleRuleUtil
def getPayscaleDetailByPayhead(payheadName,paystrObj):      
    try:  
         for payscaleDetailObj in paystrObj.getPayHeader().getPayscaleDetailses():  
             if payscaleDetailObj.getSalaryCodes().getHead()==payheadName:  
                    return (payscaleDetailObj)  
    except Exception,e:  
            raise ValidationException,[ValidationError(''Paystructure is not proper one'',''Paystructure is not proper one'')]      
    return(None)          
basicAmnt=0.0  
gradepayAmnt=0.0  
result=0.0  
validationErrors=None  
employee=currPay.getEmployee()
dateJoin=employee.getDateOfFirstAppointment()
dateNow=currPay.getFromDate()
noOfYearsForEmp=Integer(0)
noOfYearsForEmp=PayscaleRuleUtil.compareDateForPayslip(dateJoin, dateNow)
if noOfYearsForEmp>1:
    if paystrObj.getCurrBasicPay()!=None:  
        basicAmnt=paystrObj.getCurrBasicPay().intValue()  
    elif paystrObj.getDailyPay()!=None:  
        basicAmnt=paystrObj.getDailyPay().intValue()  
    try:  
        payscaleDetail=getPayscaleDetailByPayhead(''GradePay'',paystrObj)  
        if payscaleDetail != None:  
            if payscaleDetail.getSalaryCodes().getCalType()==''MonthlyFlatRate'':  
                gradepayAmnt=payscaleDetail.getAmount().intValue()  
                result=((basicAmnt+gradepayAmnt)*(20.0/100.0))  
            else:  
                validationErrors=''GradePay not there in payscale''
    except ValidationException,e:     
        validationErrors=e.getErrors()      
result,validationErrors', TO_DATE('01/01/1900 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('01/01/2100 00:00:00', 'MM/DD/YYYY HH24:MI:SS'));

INSERT INTO EG_WF_ACTIONS ( ID, TYPE, NAME, DESCRIPTION, CREATED_BY, CREATED_DATE, MODIFIED_BY,MODIFIED_DATE ) VALUES ( 
EG_WF_ACTIONS_SEQ.NEXTVAL, 'PayScaleHeader', 'IncrementBasicGradepayForProbation', 'Increment Basic Plus Gradepay for probation', NULL, NULL, NULL, NULL); 
COMMIT ;

#DOWN

