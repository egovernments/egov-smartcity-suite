#UP
update eg_script set script='
result = []
transitions={"default":["save","forward"],"COMMISSIONER":["approve"]} 
desig = wfItem.getCurrentState().getOwner().getDesigId().getDesignationName().upper()
if desig=="COMMISSIONER":
	result = transitions[desig]
else:
	result = transitions["default"]
result' where name='BudgetReAppropriationMisc.workflow.validactions';

update eg_script set script='
from java.util import List   
from org.egov.infstr import ValidationError   
from org.egov.infstr import ValidationException   
from  org.egov.exceptions import NoSuchObjectException   
from org.egov.exceptions import TooManyValuesException   
from org.egov import EGOVRuntimeException   
from org.egov.pims.utils import EisManagersUtill 
from java.lang import Integer  
from org.egov.pims.commons.service import EisCommonsManagerBean   
eisCommonMgr=EisManagersUtill.getEisCommonsManager()  
eisManagerBean = EisManagersUtill.getEisManager()
result=[]
def save():   
	return (persistenceService.persist(wfItem),None)   
def forward():   
    try:   
        pos = eisCommonMgr.getPositionByUserId(Integer.valueOf(action.getName().split(''|'')[1]))
        pi = eisManagerBean.getEmpForUserId(Integer.valueOf(action.getName().split(''|'')[1]))
        wfItem.changeState("Forwarded to "+pi.getName(),pos,comments) 
        return (persistenceService.persist(wfItem),None)   
    except ValidationException,e:   
        return (None,e.getErrors())   
def approve():   
    try:   
        pos=eisCommonMgr.getPositionByUserId(Integer.valueOf(action.getName().split(''|'')[1]))   
        wfItem.changeState("END",pos,comments)   
        return (persistenceService.persist(wfItem),None)   
    except ValidationException,e:   
        return (None,e.getErrors())   
transitions={"save":save,"forward":forward,"approve":approve}  
result,validationErrors=transitions[action.getName().split(''|'')[0]]()' where name='BudgetReAppropriation.workflow';

update eg_script set script='
from java.util import List   
from org.egov.infstr import ValidationError   
from org.egov.infstr import ValidationException   
from  org.egov.exceptions import NoSuchObjectException   
from org.egov.exceptions import TooManyValuesException   
from org.egov import EGOVRuntimeException   
from org.egov.pims.utils import EisManagersUtill 
from java.lang import Integer  
from org.egov.pims.commons.service import EisCommonsManagerBean   
eisCommonMgr=EisManagersUtill.getEisCommonsManager()  
result=[]
eisManagerBean = EisManagersUtill.getEisManager()
def save():   
	return (persistenceService.persist(wfItem),None)   
def forward():   
    try:   
        pos = eisCommonMgr.getPositionByUserId(Integer.valueOf(action.getName().split(''|'')[1]))
        pi = eisManagerBean.getEmpForUserId(Integer.valueOf(action.getName().split(''|'')[1]))
        wfItem.changeState("Forwarded to "+pi.getName(),pos,comments) 
        return (persistenceService.persist(wfItem),None)   
    except ValidationException,e:   
        return (None,e.getErrors())   
def approve():   
    try:   
        pos=eisCommonMgr.getPositionByUserId(Integer.valueOf(action.getName().split(''|'')[1]))   
        wfItem.changeState("END",pos,comments)   
        return (persistenceService.persist(wfItem),None)   
    except ValidationException,e:   
        return (None,e.getErrors())   
transitions={"save":save,"forward":forward,"approve":approve}  
result,validationErrors=transitions[action.getName().split(''|'')[0]]()' where name='BudgetReAppropriationMisc.workflow';
#DOWN



