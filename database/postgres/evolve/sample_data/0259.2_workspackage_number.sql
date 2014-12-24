#UP
update eg_script set script=
'result=worksPackage.getUserDepartment().getDeptCode()+"/TN/"+worksPackage.getPackageNumberWithoutWP()'
where name = 'workspackage.negotiationNumber.generator';
update eg_script set script=
'result=worksPackage.getUserDepartment().getDeptCode()+"/WP/"+sequenceGenerator.getNextNumber("WORKSPACKAGE_NUMBER",1).getFormattedNumber().zfill(4)+"/"+finYear.getFinYearRange()'
where name='works.wpNumber.generator';
#DOWN
update eg_script set script=
'result=worksPackage.getUserDepartment().getDeptCode()+"/"+sequenceGenerator.getNextNumber("NEGOTIATION_NUMBER",1).getFormattedNumber().zfill(4)+"/"+finYear.getFinYearRange()'
where name = 'workspackage.negotiationNumber.generator';
update eg_script set script=
'result="WP"+"/"+sequenceGenerator.getNextNumber("WORKSPACKAGE_NUMBER",1).getFormattedNumber().zfill(4)'
where name='works.wpNumber.generator';
