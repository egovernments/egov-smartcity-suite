#UP

UPDATE EG_SCRIPT SET SCRIPT=
'finSourceCode=''''    
deptCode=estimate.getExecutingDepartment().getDeptCode()  
if(deptCode==None):  
    deptCode=''-''  
for finDetail in estimate.getFinancialDetails():    
    if finDetail:    
        finSourceCode=finDetail.getMaxFinancingSource().getFundSource().getCode()    
result=finSourceCode+"/"+deptCode+"/"+sequenceGenerator.getNextNumber("PROJECTCODE-"+finYear.getFinYearRange(),1).getFormattedNumber().zfill(4)+"/"+finYear.getFinYearRange()'
WHERE NAME='works.projectcode.generator'

#DOWN

UPDATE EG_SCRIPT SET SCRIPT=
'finSourceCode=''    
deptCode=estimate.getExecutingDepartment().getDeptCode()  
if(deptCode==None):  
    deptCode='-'  
for finDetail in estimate.getFinancialDetails():    
    if finDetail:    
        finSourceCode=finDetail.getMaxFinancingSource().getFundSource().getCode()    
result=finSourceCode+"/"+deptCode+"/"+sequenceGenerator.getNextNumber(estimate.getType().getExpenditureType().getValue(),1).getFormattedNumber().zfill(4)+"/"+finYear.getFinYearRange()'
WHERE NAME='works.projectcode.generator'
