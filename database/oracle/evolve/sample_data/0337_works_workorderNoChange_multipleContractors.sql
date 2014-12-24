#UP

UPDATE EG_SCRIPT SET SCRIPT='
tenderResponse=persistenceService.find("from TenderResponse where negotiationNumber=? and state.previous.value!=''CANCELLED''",[workOrder.getNegotiationNumber()]) 
if tenderResponse.getTenderResponseContractors().size()==1: 
	result=worksPackage.getUserDepartment().getDeptCode()+"/WO/"+worksPackage.getPackageNumberWithoutWP()
else:
	woCount=persistenceService.find("select count(*)+1 from WorkOrder where negotiationNumber=?",[workOrder.getNegotiationNumber()]) 	
	wpNo=worksPackage.getPackageNumberWithoutWP().split("/") 
	result=worksPackage.getUserDepartment().getDeptCode()+"/WO/"+wpNo[0]+"-"+woCount.toString()+"/"+wpNo[1]
' where NAME='workordernumber.for.workspackage';

#DOWN

UPDATE EG_SCRIPT SET SCRIPT='result=worksPackage.getUserDepartment().getDeptCode()+"/WO/"+worksPackage.getPackageNumberWithoutWP()' where NAME='workordernumber.for.workspackage';

