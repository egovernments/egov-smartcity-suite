#UP

UPDATE EG_SCRIPT set SCRIPT=
'result=workOrderEstimate.getEstimate().getExecutingDepartment().getDeptCode()+"/"+sequenceGenerator.getNextNumber("CONTRACTORBILL",1).getFormattedNumber().zfill(4)+"/"+finYear.getFinYearRange()
'
WHERE NAME='works.contractorBillNumber.generator';
#DOWN

UPDATE EG_SCRIPT set SCRIPT=
'result=workOrder.getAbstractEstimate().getExecutingDepartment().getDeptCode()+"/"+sequenceGenerator.getNextNumber("CONTRACTORBILL",1).getFormattedNumber().zfill(4)+"/"+finYear.getFinYearRange()
'
WHERE NAME='works.contractorBillNumber.generator';


