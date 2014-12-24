#UP

UPDATE EG_SCRIPT 
SET SCRIPT='if not estimate.getFinancialDetails().isEmpty() and  estimate.getFinancialDetails().get(0):
	 if estimate.getWard().getBoundaryType().getName()==''City'':
		result="RTS/"+estimate.getExecutingDepartment().getDeptCode()+"/HQ/"+sequenceGenerator.getNextNumber("TECHNICALSANCTION_NUMBER",1).getFormattedNumber().zfill(4)+"/"+estimate.getFinancialDetails().get(0).getFunction().getCode() 
	 if estimate.getWard().getBoundaryType().getName()==''Zone'' or estimate.getWard().getBoundaryType().getName()==''Ward'':
		result="RTS/"+estimate.getExecutingDepartment().getDeptCode()+"/ZONE/"+sequenceGenerator.getNextNumber("TECHNICALSANCTION_NUMBER",1).getFormattedNumber().zfill(4)+"/"+estimate.getFinancialDetails().get(0).getFunction().getCode() 
else:
	if estimate.getWard().getBoundaryType().getName()==''City'': 
		result="RTS/"+estimate.getExecutingDepartment().getDeptCode()+"/HQ/"+sequenceGenerator.getNextNumber("TECHNICALSANCTION_NUMBER",1).getFormattedNumber().zfill(4)
	if estimate.getWard().getBoundaryType().getName()==''Zone'' or estimate.getWard().getBoundaryType().getName()==''Ward'': 
		result="RTS/"+estimate.getExecutingDepartment().getDeptCode()+"/ZONE/"+sequenceGenerator.getNextNumber("TECHNICALSANCTION_NUMBER",1).getFormattedNumber().zfill(4)'
where NAME='works.revisionEstimate.techsanctionnumber.generator';  


#DOWN
UPDATE EG_SCRIPT 
SET SCRIPT='if not estimate.getFinancialDetails().isEmpty() and  estimate.getFinancialDetails().get(0):   
	result="RTS/"+estimate.getExecutingDepartment().getDeptCode()+"/"+sequenceGenerator.getNextNumber("TECHNICALSANCTION_NUMBER",1).getFormattedNumber().zfill(4)+"/"+estimate.getFinancialDetails().get(0).getFunction().getCode()  
else:  
	result="RTS/"+estimate.getExecutingDepartment().getDeptCode()+"/"+sequenceGenerator.getNextNumber("TECHNICALSANCTION_NUMBER",1).getFormattedNumber().zfill(4)'
where NAME='works.revisionEstimate.techsanctionnumber.generator'; 

