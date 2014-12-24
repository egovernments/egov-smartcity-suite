#UP
update EG_SCRIPT set script='from org.egov.infstr import ValidationError
from org.egov.pims.utils import EisManagersUtill
from java.lang import Integer
from java.util import Date
from org.egov.infstr.client.filter import EGOVThreadLocals
eisManager=EisManagersUtill.getEisManager()
eisCommonMgr=EisManagersUtill.getEisCommonsManager()
def approve():
    funcryDesgText =''Approved by ''+ getLoginUserFuncryAndDesgText()
    update_workflow(wfItem,funcryDesgText.upper(),comments)
    update_workflow(wfItem,''END'',''Expense Bill approved workflow ends'')
    egwstatus = persistenceService.find("from EgwStatus where moduletype=? and description=?",["ASSETREVALUATION","RevaluataionApproved"])
    wfItem.setStatus(egwstatus)
    return (persistenceService.persist(wfItem),None)
def update_workflow(wfItem,wfItemStatus,comments):
    position=eisCommonMgr.getPositionByUserId(Integer.valueOf(action.getName().split(''|'')[1]))
    wfItem.changeState(wfItemStatus,position,comments)
def getLoginUserFuncryAndDesgText():
    personalInfo = eisManager.getEmpForUserId(Integer.valueOf(EGOVThreadLocals.getUserId()))
    assignment = eisManager.getAssignmentByEmpAndDate(Date(),personalInfo.getIdPersonalInformation())
    funcryDesgText = ''''
    if(assignment.functionary != None):
      funcryDesgText=  assignment.functionary.name  + '' ''
    if(assignment.desigId != None):
      funcryDesgText = funcryDesgText + assignment.desigId.designationName
    return funcryDesgText
transitions={''approve'':approve}
result,validationErrors=transitions[action.getName().split(''|'')[0]]()'
where name='AssetRevaluation.workflow';
#DOWN
update EG_SCRIPT set script='from org.egov.infstr import ValidationError
from org.egov.pims.utils import EisManagersUtill
from java.lang import Integer
from java.util import Date
from org.egov.infstr.client.filter import EGOVThreadLocals
eisManager=EisManagersUtill.getEisManager()
eisCommonMgr=EisManagersUtill.getEisCommonsManager()
def approve():
    funcryDesgText =''Approved by ''+ getLoginUserFuncryAndDesgText()
    update_workflow(wfItem,funcryDesgText.upper(),comments)
    update_workflow(wfItem,''END'',''Expense Bill approved workflow ends'')
    egwstatus = persistenceService.find("from EgwStatus where moduletype=? and description=?",["ASSET","ActivityApproved"])
    wfItem.setStatus(egwstatus)
    return (persistenceService.persist(wfItem),None)
def update_workflow(wfItem,wfItemStatus,comments):
    position=eisCommonMgr.getPositionByUserId(Integer.valueOf(action.getName().split(''|'')[1]))
    wfItem.changeState(wfItemStatus,position,comments)
def getLoginUserFuncryAndDesgText():
    personalInfo = eisManager.getEmpForUserId(Integer.valueOf(EGOVThreadLocals.getUserId()))
    assignment = eisManager.getAssignmentByEmpAndDate(Date(),personalInfo.getIdPersonalInformation())
    funcryDesgText = ''''
    if(assignment.functionary != None):
      funcryDesgText=  assignment.functionary.name  + '' ''
    if(assignment.desigId != None):
      funcryDesgText = funcryDesgText + assignment.desigId.designationName
    return funcryDesgText
transitions={''approve'':approve}
result,validationErrors=transitions[action.getName().split(''|'')[0]]()'
where name='AssetRevaluation.workflow';