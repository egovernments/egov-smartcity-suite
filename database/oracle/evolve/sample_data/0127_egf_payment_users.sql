#UP

INSERT INTO EG_POSITION ( POSITION_NAME, ID, SANCTIONED_POSTS, OUTSOURCED_POSTS, DESIG_ID,EFFECTIVE_DATE ) VALUES ( 
'UAC-SM', seq_pos.nextval, 1, 1, (select designationid from eg_designation where designation_name='SECTION MANAGER'),  TO_Date( '08/15/1947 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM')); 
INSERT INTO EG_POSITION ( POSITION_NAME, ID, SANCTIONED_POSTS, OUTSOURCED_POSTS, DESIG_ID,EFFECTIVE_DATE ) VALUES ( 
'UAC-AO', seq_pos.nextval, 1, 1, (select designationid from eg_designation where designation_name='ACCOUNTS OFFICER'),  TO_Date( '08/15/1947 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM')); 

INSERT INTO EG_USER(ID_USER, FIRST_NAME, USER_NAME, PWD, PWD_REMINDER, UPDATETIME, IS_SUSPENDED, ISACTIVE, FROMDATE, TODATE)
 VALUES (seq_eg_user.NEXTVAL, 'SMUAC', 'SMUAC', 't27o223b7q3k0mtic20k1u32n', 'egovfinancials', TO_DATE('03/17/2008 11:59:39', 'MM/DD/YYYY HH24:MI:SS'), 'N', 1, TO_DATE('01/01/2006 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('01/01/2011 00:00:00', 'MM/DD/YYYY HH24:MI:SS'));
INSERT INTO EG_USER(ID_USER, FIRST_NAME, USER_NAME, PWD, PWD_REMINDER, UPDATETIME, IS_SUSPENDED, ISACTIVE, FROMDATE, TODATE)
 VALUES (seq_eg_user.NEXTVAL, 'AOUAC', 'AOUAC', 't27o223b7q3k0mtic20k1u32n', 'egovfinancials', TO_DATE('03/17/2008 11:59:39', 'MM/DD/YYYY HH24:MI:SS'), 'N', 1, TO_DATE('01/01/2006 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('01/01/2011 00:00:00', 'MM/DD/YYYY HH24:MI:SS'));
INSERT INTO EG_EMPLOYEE (ID, CODE, DATE_OF_BIRTH, BLOOD_GROUP, MOTHER_TONUGE, RELIGION_ID, COMMUNITY_ID, GENDER, IS_HANDICAPPED, IS_MED_REPORT_AVAILABLE, DATE_OF_FIRST_APPOINTMENT, IDENTIFICATION_MARKS1, LANGUAGES_KNOWN_ID, MODE_OF_RECRUIMENT_ID, RECRUITMENT_TYPE_ID, CATEGORY_ID, QULIFIED_ID, SALARY_BANK, BANK,  PAY_FIXED_IN_ID, GRADE_ID, PRESENT_DESIGNATION, SCALE_OF_PAY, BASIC_PAY, SPL_PAY, PP_SGPP_PAY, ANNUAL_INCREMENT_ID, GPF_AC_NUMBER, RETIREMENT_AGE, PRESENT_DEPARTMENT, IF_ON_DUTY_ARRANGMENT_DUTY_DEP, LOCATION, COST_CENTER, ID_DEPT, ID_USER, ISACTIVE, EMPFATHER_FIRSTNAME, EMPFATHER_LASTNAME, EMPFATHER_MIDDLENAME, EMP_FIRSTNAME, EMP_LASTNAME, EMP_MIDDLENAME, IDENTIFICATION_MARKS2, PAN_NUMBER, NAME, MATURITY_DATE,EMPCATMSTR_ID)
 VALUES (EGPIMS_PERSONAL_INFO_SEQ.NEXTVAL, '2002', TO_DATE('11/11/1982 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), NULL, NULL, 
NULL, NULL, '0', '0', '0', TO_DATE('11/11/2007 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), NULL, NULL, 1, 1,NULL, NULL, NULL, NULL, NULL,NULL, NULL, NULL,  NULL,NULL, NULL, NULL,  '5111', NULL, NULL, NULL, NULL, NULL, 
NULL, (SELECT id_user FROM EG_USER WHERE user_name ='SMUAC'), 1, NULL, NULL,NULL, 'SMUAC', NULL, NULL, NULL,NULL, 'SMUAC', NULL,3);

INSERT INTO EG_EMPLOYEE (ID, CODE, DATE_OF_BIRTH, BLOOD_GROUP, MOTHER_TONUGE, RELIGION_ID, COMMUNITY_ID, GENDER, IS_HANDICAPPED, IS_MED_REPORT_AVAILABLE, DATE_OF_FIRST_APPOINTMENT, IDENTIFICATION_MARKS1, LANGUAGES_KNOWN_ID, MODE_OF_RECRUIMENT_ID, RECRUITMENT_TYPE_ID, CATEGORY_ID, QULIFIED_ID, SALARY_BANK, BANK,  PAY_FIXED_IN_ID, GRADE_ID, PRESENT_DESIGNATION, SCALE_OF_PAY, BASIC_PAY, SPL_PAY, PP_SGPP_PAY, ANNUAL_INCREMENT_ID, GPF_AC_NUMBER, RETIREMENT_AGE, PRESENT_DEPARTMENT, IF_ON_DUTY_ARRANGMENT_DUTY_DEP, LOCATION, COST_CENTER, ID_DEPT, ID_USER, ISACTIVE, EMPFATHER_FIRSTNAME, EMPFATHER_LASTNAME, EMPFATHER_MIDDLENAME, EMP_FIRSTNAME, EMP_LASTNAME, EMP_MIDDLENAME, IDENTIFICATION_MARKS2, PAN_NUMBER, NAME, MATURITY_DATE,EMPCATMSTR_ID)
 VALUES (EGPIMS_PERSONAL_INFO_SEQ.NEXTVAL, '2003', TO_DATE('11/11/1982 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), NULL, NULL, 
NULL, NULL, '0', '0', '0', TO_DATE('11/11/2007 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), NULL, NULL, 1, 1,NULL, NULL, NULL, NULL, NULL,NULL, NULL, NULL,  NULL,NULL, NULL, NULL,  '5111', NULL, NULL, NULL, NULL, NULL, 
NULL, (SELECT id_user FROM EG_USER WHERE user_name ='AOUAC'), 1, NULL, NULL,NULL, 'AOUAC', NULL, NULL, NULL,NULL, 'AOUAC', NULL,3);
INSERT INTO EG_EMP_ASSIGNMENT_PRD (ID, FROM_DATE, TO_DATE, ID_EMPLOYEE)
 VALUES (seq_ass_prd.NEXTVAL, TO_DATE('01/01/1990 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), NULL, (SELECT id FROM EG_EMPLOYEE WHERE name='SMUAC'));
INSERT INTO EG_EMP_ASSIGNMENT_PRD (ID, FROM_DATE, TO_DATE, ID_EMPLOYEE)
 VALUES (seq_ass_prd.NEXTVAL, TO_DATE('01/01/1990 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), NULL, (SELECT id FROM EG_EMPLOYEE WHERE name='AOUAC'));
INSERT INTO EG_EMP_ASSIGNMENT (ID, ID_FUND, ID_FUNCTION, DESIGNATIONID, ID_FUNCTIONARY, PCT_ALLOCATION, REPORTS_TO, ID_EMP_ASSIGN_PRD, FIELD, MAIN_DEPT, POSITION_ID,GOVT_ORDER_NO, GRADE_ID)
 VALUES (seq_ass.NEXTVAL, 11, 1, (select designationid from eg_designation where designation_name='SECTION MANAGER'), 
    (select id from functionary where name='UAC'), NULL, NULL,(SELECT id FROM EG_EMP_ASSIGNMENT_PRD WHERE id_employee =(SELECT id FROM EG_EMPLOYEE WHERE name='SMUAC')), NULL, 
    (select id_dept from eg_department where dept_code='H'), (SELECT id FROM EG_POSITION WHERE position_name='UAC-SM'),NULL,NULL);
INSERT INTO EG_EMP_ASSIGNMENT (ID, ID_FUND, ID_FUNCTION, DESIGNATIONID, ID_FUNCTIONARY, PCT_ALLOCATION, REPORTS_TO, ID_EMP_ASSIGN_PRD, FIELD, MAIN_DEPT, POSITION_ID,GOVT_ORDER_NO, GRADE_ID)
 VALUES (seq_ass.NEXTVAL, 11, 1, (select designationid from eg_designation where designation_name='ACCOUNTS OFFICER'), 
    (select id from functionary where name='UAC'), NULL, NULL,(SELECT id FROM EG_EMP_ASSIGNMENT_PRD WHERE id_employee =(SELECT id FROM EG_EMPLOYEE WHERE name='AOUAC')), NULL, 
    (select id_dept from eg_department where dept_code='H'), (SELECT id FROM EG_POSITION WHERE position_name='UAC-AO'),NULL,NULL);

INSERT INTO EG_USER_JURLEVEL(ID_USER_JURLEVEL, ID_USER, ID_BNDRY_TYPE, UPDATETIME)
 VALUES (SEQ_EG_USER_JURLEVEL.NEXTVAL, (SELECT id_user FROM EG_USER WHERE first_name='SMUAC'), 1, TO_DATE('08/11/2009 00:00:00', 'MM/DD/YYYY HH24:MI:SS'));
INSERT INTO EG_USER_JURLEVEL(ID_USER_JURLEVEL, ID_USER, ID_BNDRY_TYPE, UPDATETIME)
 VALUES (SEQ_EG_USER_JURLEVEL.NEXTVAL, (SELECT id_user FROM EG_USER WHERE first_name='AOUAC'), 1, TO_DATE('08/11/2009 00:00:00', 'MM/DD/YYYY HH24:MI:SS'));
INSERT INTO EG_USER_JURVALUES (ID_USER_JURLEVEL, ID_BNDRY, ID, FROMDATE, TODATE, IS_HISTORY)
 VALUES ((SELECT id_user_jurlevel FROM EG_USER_JURLEVEL WHERE id_user=(SELECT id_user FROM EG_USER WHERE first_name='SMUAC')), 1, SEQ_EG_USER_JURVALUES.NEXTVAL, TO_DATE('08/11/2009 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('08/11/2099 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 'N');
INSERT INTO EG_USER_JURVALUES (ID_USER_JURLEVEL, ID_BNDRY, ID, FROMDATE, TODATE, IS_HISTORY)
 VALUES ((SELECT id_user_jurlevel FROM EG_USER_JURLEVEL WHERE id_user=(SELECT id_user FROM EG_USER WHERE first_name='AOUAC')), 1, SEQ_EG_USER_JURVALUES.NEXTVAL, TO_DATE('08/11/2009 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('08/11/2099 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 'N');
INSERT INTO EG_USERROLE (id_role, id_user, ID, fromdate, todate, is_history)
VALUES ((SELECT id_role FROM EG_ROLES WHERE ROLE_NAME='SuperUser'), (SELECT id_user FROM EG_USER WHERE FIRST_NAME = 'SMUAC'), seq_eg_userrole.NEXTVAL,TO_DATE('01-01-2000','mm-dd-yyyy'), '', 'N' );
INSERT INTO EG_USERROLE (id_role, id_user, ID, fromdate, todate, is_history)
VALUES ((SELECT id_role FROM EG_ROLES WHERE ROLE_NAME='SuperUser'), (SELECT id_user FROM EG_USER WHERE FIRST_NAME = 'AOUAC'), seq_eg_userrole.NEXTVAL,TO_DATE('01-01-2000','mm-dd-yyyy'), '', 'N' );


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
pimsDAO=EisDAOFactory.getDAOFactory().getPersonalInformationDAO()
empDeptDAO=EisDAOFactory.getDAOFactory().getEmployeeDepartmentDAO()
def aa_approve():
    pos=find_posForDesignation(''SECTION MANAGER'',wfItem)
    wfItem.changeState(''AA APPROVED'',pos,comments)
    return (persistenceService.persist(wfItem),None)
def am_approve():
    pos=find_posForDesignation(''ACCOUNTS OFFICER'',wfItem)
    wfItem.changeState(''AM APPROVED'',pos,comments)
    return (persistenceService.persist(wfItem),None)
def ao_approve():
    pos=find_posForDesignation(''ACCOUNTS OFFICER'',wfItem)
    wfItem.changeState(''END'',pos,comments)
    persistenceService.createVoucherfromPreApprovedVoucher(wfItem)
    return (persistenceService.persist(wfItem),None)
def ao_reject():
    pos=find_posForDesignation(''SECTION MANAGER'',wfItem)
    wfItem.changeState(''AO REJECTED'',pos,comments)
    return (persistenceService.persist(wfItem),None)
def am_reject():
    pos=find_posForDesignation(''ASSISTANT'',wfItem)
    wfItem.changeState(''AM REJECTED'',pos,comments)
    return (persistenceService.persist(wfItem),None)
def find_desig(designationName):
    designations=persistenceService.findAllBy(''from DesignationMaster dm where designationName=?'',[designationName])
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
transitions={''aa_approve'':aa_approve,''am_approve'':am_approve,''ao_approve'':ao_approve,''ao_reject'':ao_reject,''am_reject'':am_reject}
result,validationErrors=transitions[action.getName()]()'
where name='CVoucherHeader.workflow';


update eg_emp_assignment set main_dept=(select id_dept from eg_department where dept_code='A') where position_id=(select id from eg_position where position_name='Assistant');
update eg_emp_assignment set main_dept=(select id_dept from eg_department where dept_code='A') where position_id=(select id from eg_position where position_name='Acc Manager');
update eg_emp_assignment set main_dept=(select id_dept from eg_department where dept_code='A') where position_id=(select id from eg_position where position_name='Acc Officer');

#DOWN
