#UP

Insert into eg_script (ID,NAME,SCRIPT_TYPE,CREATED_BY,CREATED_DATE,MODIFIED_BY,MODIFIED_DATE,SCRIPT,START_DATE,END_DATE) 
values (eg_script_seq.nextval,'budget.report.view.access','python',null,null,null,null,'result=['' '','' '' ]',to_date('01-04-2000','dd-MM-yyyy'),to_date('01-04-2100','dd-MM-yyyy'));

update eg_script set script='
from org.egov.pims.dao import EisDAOFactory
from java.util import Date
result=False
employee = eisManagerBean.getEmpForUserId(userId)        
assignment = eisManagerBean.getAssignmentByEmpAndDate(Date(),employee.getIdPersonalInformation())   
designation = assignment.desigId.designationName.upper()
functionary = ""
if(assignment.functionary!=None):
    state=assignment.functionary.name
if(designation in ["FINANCIAL ADVISOR","DEPUTY COMMISSIONER","COMMISSIONER"] or (wfItem!=None and wfItem.getCurrentState()!=None and wfItem.getCurrentState().value.upper() == "END") or functionary.upper() == "FMU"):
	result=True
' where name='budget.report.view.access';

#DOWN
delete from eg_script where NAME='budget.report.view.access';


