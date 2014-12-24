#UP

UPDATE EG_SCRIPT 
SET SCRIPT=
'if estimate.getWard().getBoundaryType().getName()==''City'':
		result=estimate.getExecutingDepartment().getDeptCode()+"/TS/HQ/"+sequenceGenerator.getNextNumber("TECHNICALSANCTION_NUMBER",1).getFormattedNumber().zfill(4)+"/"+finYear.getFinYearRange()
if estimate.getWard().getBoundaryType().getName()==''Zone'' or estimate.getWard().getBoundaryType().getName()==''Ward'':
		result=estimate.getExecutingDepartment().getDeptCode()+"/TS/ZONE/"+sequenceGenerator.getNextNumber("TECHNICALSANCTION_NUMBER",1).getFormattedNumber().zfill(4)+"/"+finYear.getFinYearRange()'
where NAME='works.techsanctionnumber.generator';  


#DOWN

UPDATE EG_SCRIPT 
SET SCRIPT=
'if not estimate.getFinancialDetails().isEmpty() and  estimate.getFinancialDetails().get(0):
	 if estimate.getWard().getBoundaryType().getName()==''City'':
		result=estimate.getExecutingDepartment().getDeptCode()+"/TS/HQ/"+sequenceGenerator.getNextNumber("TECHNICALSANCTION_NUMBER",1).getFormattedNumber().zfill(4)+"/"+estimate.getFinancialDetails().get(0).getFunction().getCode()
	 if estimate.getWard().getBoundaryType().getName()==''Zone'' or estimate.getWard().getBoundaryType().getName()==''Ward'':
		result=estimate.getExecutingDepartment().getDeptCode()+"/TS/ZONE/"+sequenceGenerator.getNextNumber("TECHNICALSANCTION_NUMBER",1).getFormattedNumber().zfill(4)+"/"+estimate.getFinancialDetails().get(0).getFunction().getCode()
else:
	if estimate.getWard().getBoundaryType().getName()==''City'':
		result=estimate.getExecutingDepartment().getDeptCode()+"/TS/HQ/"+sequenceGenerator.getNextNumber("TECHNICALSANCTION_NUMBER",1).getFormattedNumber().zfill(4)
	if estimate.getWard().getBoundaryType().getName()==''Zone'' or estimate.getWard().getBoundaryType().getName()==''Ward'':
		result=estimate.getExecutingDepartment().getDeptCode()+"/TS/ZONE/"+sequenceGenerator.getNextNumber("TECHNICALSANCTION_NUMBER",1).getFormattedNumber().zfill(4)'
where NAME='works.techsanctionnumber.generator'; 


