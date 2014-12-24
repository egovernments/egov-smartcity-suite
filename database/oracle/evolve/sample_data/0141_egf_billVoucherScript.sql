#UP
update eg_script set script='
from org.egov.pims.dao import EisDAOFactory
from org.egov.pims.commons import DesignationMaster
from org.egov.pims.commons.dao import DesignationMasterDAO
from org.egov.infstr import ValidationError
from org.egov.infstr import ValidationException
from org.egov.lib.rjbac.dept import DepartmentImpl
from org.egov.exceptions import NoSuchObjectException
from org.egov.exceptions import TooManyValuesException
from org.egov import EGOVRuntimeException
from org.egov.infstr.utils import EGovConfig
from org.egov.dao.bills import BillsDaoFactory
pimsDAO=EisDAOFactory.getDAOFactory().getPersonalInformationDAO()
empDeptDAO=EisDAOFactory.getDAOFactory().getEmployeeDepartmentDAO()
egBillRegDao=BillsDaoFactory.getDAOFactory().getEgBillRegisterHibernateDAO()
expType=egBillRegDao.getBillTypeforVoucher(wfItem)
def aa_approve():
 if (expType == ''Purchase'' or expType == ''Contingency'' or expType == ''Works'' or expType == ''Salary''):
    update_workflow(''SECTION MANAGER'',wfItem,''AA APPROVED'',comments)
    return (persistenceService.persist(wfItem),None)
def am_approve():
 if (expType == ''Purchase'' or expType == ''Contingency'' or expType == ''Works'' or expType == ''Salary''):
    update_workflow(''ACCOUNTS OFFICER'',wfItem,''AM APPROVED'',comments)
    return (persistenceService.persist(wfItem),None)
def ao_approve():
 if (expType == ''Purchase'' or expType == ''Contingency'' or expType == ''Works'' or expType == ''Salary''):
     update_workflow(''ACCOUNTS OFFICER'',wfItem,''END'',comments)
     persistenceService.createVoucherfromPreApprovedVoucher(wfItem)
     return (persistenceService.persist(wfItem),None)
def ao_reject():
 if (expType == ''Purchase'' or expType == ''Contingency'' or expType == ''Works'' or expType == ''Salary''):
    update_workflow(''SECTION MANAGER'',wfItem,''AO REJECTED'',comments)
    return (persistenceService.persist(wfItem),None)
def am_reject():
 if (expType == ''Purchase'' or expType == ''Contingency'' or expType == ''Works'' or expType == ''Salary''):
   update_workflow(''ASSISTANT'',wfItem,''AM REJECTED'',comments)
   return (persistenceService.persist(wfItem),None)
def find_desig(designationName):
    designations=persistenceService.'
where name='CVoucherHeader.workflow';
update eg_script set script=script||'findAllBy(''from DesignationMaster dm where designationName=?'',[designationName])
    if not designations: raise ValidationException,[ValidationError(''currentState.owner'',''egf.preapprovedJV.no_next_desig'')]
    return designations[0]
def find_posForDesignation(designation, wfItem):
    next_desig=find_desig(designation)
    if not next_desig:
        raise ValidationException,[ValidationError(''currentState.owner'',''egf.preapprovedJV.no_designation'')]
    emp=None
    user=None
    try:
        dept=persistenceService.find(''FROM DepartmentImpl DI WHERE DI.id =?'',[wfItem.getVouchermis().getDepartmentid().getId()])
        btypeStr=EGovConfig.getProperty("egf_config.xml", "city", "1", "BoundaryType")
        htype=persistenceService.find("from HeirarchyTypeImpl where lower(name)=lower(?)",["Administration"])
        sqlQry="from BoundaryTypeImpl btype where lower(btype.name)=lower(''"+btypeStr+"'') and btype.heirarchyType=?"
        btype=persistenceService.find(sqlQry,[htype])
        bndry=persistenceService.find("from BoundaryImpl bndry where bndry.boundaryType=?",[btype])
        functionary = persistenceService.find("from Functionary where name=?",["UAC"])
        emp=pimsDAO.getEmployeeByFunctionary(dept.getId(),next_desig.getDesignationId(),bndry.getId(),functionary.getId())
        pos=persistenceService.getPositionForEmployee(emp)
    except TooManyValuesException:
        raise ValidationException,[ValidationError(''TooManyValuesException'',''TooManyValuesException'')]
    except NoSuchObjectException:
        raise ValidationException,[ValidationError(''NoSuchObjectException'',''NoSuchObjectException'')]
    except EGOVRuntimeException:
        raise ValidationException,[ValidationError(''EGOVRuntimeException'',''TooManyValuesException'')]
    else:
        pass
    if not pos:
        raise ValidationException,[ValidationError(''currentState.owner'',''egf.preapprovedJV.no_position'')]
    return pos
def update_workflow(designation1, wfItem,designation2,comments):
    pos=find_posForDesignation(designation1,wfItem)
    wfItem.changeState(designation2,pos,comments)
transitions={''aa_approve'':aa_approve,''am_approve'':am_approve,''ao_approve'':ao_approve,''ao_reject'':ao_reject,''am_reject'':am_reject}
result,validationErrors=transitions[action.getName()]()'
where name='CVoucherHeader.workflow';
#DOWN