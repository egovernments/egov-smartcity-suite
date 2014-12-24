#UP
/*******Script for payslip workflow***************/







Insert into eg_script
   (ID, NAME, SCRIPT_TYPE, SCRIPT, START_DATE, END_DATE)
 Values
   (eg_script_seq.nextVal, 'EmpPayroll.workflow', 'python', 'from org.egov import EGOVRuntimeException
from org.egov import EGOVException
from java.lang import Exception
from org.egov.exceptions import NoSuchObjectException, TooManyValuesException
from org.egov.infstr import ValidationError, ValidationException
from org.egov.pims.dao import EisDAOFactory
pimsDAO=EisDAOFactory.getDAOFactory().getPersonalInformationDAO()  
empDeptDAO=EisDAOFactory.getDAOFactory().getEmployeeDepartmentDAO()
validationErrors=None
def user_approve():
    try:
        pos=wfItem.getState().getOwner()
        hirPos=persistenceService.getSuperiorPosition(pos,''payslip'')
        if hirPos:            
            if wfItem.getState().getValue()==''NEW'' or wfItem.getState().getValue()==''HOD_REJECT'':
                persistenceService.updatePayslipStatus(wfItem,''Created'')
                wfItem.changeState(''CLEARK_APPROVED'',hirPos,comments)
            elif wfItem.getState().getValue()==''CLEARK_APPROVED'' or wfItem.getState().getValue()==''ACCOUNTANT_REJECT'':
                persistenceService.updatePayslipStatus(wfItem,''DeptApproved'')
                wfItem.changeState(''HOD_APPROVED'',hirPos,comments)
            elif wfItem.getState().getValue()==''HOD_APPROVED''or wfItem.getState().getValue()==''CAO_REJECT'':
                persistenceService.updatePayslipStatus(wfItem,''AccountsApproved'')
                wfItem.changeState(''ACCOUNTANT_APPROVED'',hirPos,comments)
        elif wfItem.getState().getValue()==''ACCOUNTANT_APPROVED'':
            persistenceService.updatePayslipStatus(wfItem,''AuditApproved'')
            wfItem.changeState(''END'',pos,comments)
        else:            
            raise ValidationException,[ValidationError(''currentState.owner'',''There is no superior position for - ''+pos.getName()+'' -position'')]
        return (persistenceService.persist(wfItem),None)
    except EGOVException,e:        
        raise ValidationException,[ValidationError(''ExceptionInWorkflow--'',e.getMessage())]
def user_reject():
    try:
        pos=wfItem.getState().getOwner()
        lowerPos=persistenceService.getInferiorPosition(pos,''payslip'')
        if lowerPos:            
            if wfItem.getState().getValue()==''CLEARK_APPROVED'' or wfItem.getState().getValue()==''ACCOUNTANT_REJECT'':
                wfItem.changeState(''HOD_REJECT'',lowerPos,comments)
            elif wfItem.getState().getValue()==''HOD_APPROVED''or wfItem.getState().getValue()==''CAO_REJECT'':
                wfItem.changeState(''ACCOUNTANT_REJECT'',lowerPos,comments)
            elif wfItem.getState().getValue()==''ACCOUNTANT_APPROVED'':
                wfItem.changeState(''CAO_REJECT'',lowerPos,comments)
        elif wfItem.getState().getValue()==''HOD_REJECT'':
            persistenceService.updatePayslipStatus(wfItem,''Cancelled'')
            wfItem.changeState(''END'',pos,comments)
        else:            
            raise ValidationException,[ValidationError(''currentState.owner'',''There is no inferior position for - ''+pos.getName()+'' -position'')]
        return (persistenceService.persist(wfItem),None)
    except Exception,e:        
        raise ValidationException,[ValidationError(''ExceptionInWorkflow--'',e.getMessage())]
transitions={''user_approve'':user_approve,''user_reject'':user_reject}
result=None
try:
    if action.getName().endswith(''approve''):        
        result=transitions[''user_approve'']()
    elif action.getName().endswith(''reject''):     
        result=transitions[''user_reject'']()
except ValidationException, e:    
    validationErrors = e.getErrors()
except KeyError:
    validationErrors=''This -''+action.getName() +''- Action is not defined for workflow''   
result,validationErrors', TO_DATE('01/01/1900 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('01/01/2100 00:00:00', 'MM/DD/YYYY HH24:MI:SS'));



--designation and position creation
INSERT INTO EG_DESIGNATION
   (DESIGNATIONID, DEPTID, DESIGNATION_NAME, DESIGNATION_LOCAL, OFFICER_LEVEL, DESIGNATION_DESCRIPTION, SANCTIONED_POSTS, OUTSOURCED_POSTS, BASIC_FROM, BASIC_TO, ANN_INCREMENT, REPORTSTO)
 VALUES
   (SEQ_DESIGNATION.NEXTVAL, 1, 'CLEARK', NULL, 
    1, 'CLEARK.', 10, 7, 4500, 
    9000, NULL, 2);

INSERT INTO EG_DESIGNATION
   (DESIGNATIONID, DEPTID, DESIGNATION_NAME, DESIGNATION_LOCAL, OFFICER_LEVEL, DESIGNATION_DESCRIPTION, SANCTIONED_POSTS, OUTSOURCED_POSTS, BASIC_FROM, BASIC_TO, ANN_INCREMENT, REPORTSTO)
 VALUES
   (SEQ_DESIGNATION.NEXTVAL, 1, 'HOD1', NULL, 
    1, 'HOD1.', 10, 7, 4500, 
    9000, NULL, 2);

      INSERT INTO EG_DESIGNATION
   (DESIGNATIONID, DEPTID, DESIGNATION_NAME, DESIGNATION_LOCAL, OFFICER_LEVEL, DESIGNATION_DESCRIPTION, SANCTIONED_POSTS, OUTSOURCED_POSTS, BASIC_FROM, BASIC_TO, ANN_INCREMENT, REPORTSTO)
 VALUES
   (SEQ_DESIGNATION.NEXTVAL, 1, 'ACCOUNTANT', NULL, 
    1, 'ACCOUNTANT.', 10, 7, 4500, 
    9000, NULL, 2);
       INSERT INTO EG_DESIGNATION
   (DESIGNATIONID, DEPTID, DESIGNATION_NAME, DESIGNATION_LOCAL, OFFICER_LEVEL, DESIGNATION_DESCRIPTION, SANCTIONED_POSTS, OUTSOURCED_POSTS, BASIC_FROM, BASIC_TO, ANN_INCREMENT, REPORTSTO)
 VALUES
   (SEQ_DESIGNATION.NEXTVAL, 1, 'CAO', NULL, 
    1, 'CAO.', 10, 7, 4500, 
    9000, NULL, 2);




INSERT INTO EG_POSITION
   (POSITION_NAME, ID, SANCTIONED_POSTS, OUTSOURCED_POSTS, DESIG_ID, EFFECTIVE_DATE)
 VALUES
   ('Cleark_1', seq_pos.NEXTVAL, 10, 7, 
    (SELECT designationid FROM EG_DESIGNATION WHERE designation_name='CLEARK' ), TO_DATE('03/26/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'));
INSERT INTO EG_POSITION
   (POSITION_NAME, ID, SANCTIONED_POSTS, OUTSOURCED_POSTS, DESIG_ID, EFFECTIVE_DATE)
 VALUES
   ('Hod_1', seq_pos.NEXTVAL, 5, 2, 
       (SELECT designationid FROM EG_DESIGNATION WHERE designation_name='HOD1' ), TO_DATE('03/26/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'));
 

INSERT INTO EG_POSITION
   (POSITION_NAME, ID, SANCTIONED_POSTS, OUTSOURCED_POSTS, DESIG_ID, EFFECTIVE_DATE)
 VALUES
   ('Accountant_1', seq_pos.NEXTVAL, 5, 2, 
       (SELECT designationid FROM EG_DESIGNATION WHERE designation_name='ACCOUNTANT' ), TO_DATE('03/26/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'));
 
INSERT INTO EG_POSITION
   (POSITION_NAME, ID, SANCTIONED_POSTS, OUTSOURCED_POSTS, DESIG_ID, EFFECTIVE_DATE)
 VALUES
   ('Cao_1', seq_pos.NEXTVAL, 5, 2, 
       (SELECT designationid FROM EG_DESIGNATION WHERE designation_name='CAO' ), TO_DATE('03/26/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'));
 


---------------------------------user
INSERT INTO EG_USER
   (ID_USER, FIRST_NAME, USER_NAME, PWD, PWD_REMINDER, UPDATETIME, IS_SUSPENDED, ISACTIVE, FROMDATE, TODATE)
 VALUES
   (seq_eg_user.NEXTVAL, 'Cleark1', 'Cleark1', 't27o223b7q3k0mtic20k1u32n', 'egovfinancials', TO_DATE('03/17/2008 11:59:39', 'MM/DD/YYYY HH24:MI:SS'), 'N', 1, TO_DATE('01/01/2006 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('01/01/2011 00:00:00', 'MM/DD/YYYY HH24:MI:SS'));
INSERT INTO EG_USER
   (ID_USER, FIRST_NAME, USER_NAME, PWD, PWD_REMINDER, UPDATETIME, IS_SUSPENDED, ISACTIVE, FROMDATE, TODATE)
 VALUES
   (seq_eg_user.NEXTVAL, 'Hod1', 'Hod1', 't27o223b7q3k0mtic20k1u32n', 'egovfinancials', TO_DATE('03/17/2008 11:59:39', 'MM/DD/YYYY HH24:MI:SS'), 'N', 1, TO_DATE('01/01/2006 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('01/01/2011 00:00:00', 'MM/DD/YYYY HH24:MI:SS'));

INSERT INTO EG_USER
   (ID_USER, FIRST_NAME, USER_NAME, PWD, PWD_REMINDER, UPDATETIME, IS_SUSPENDED, ISACTIVE, FROMDATE, TODATE)
 VALUES
   (seq_eg_user.NEXTVAL, 'Accountant1', 'Accountant1', 't27o223b7q3k0mtic20k1u32n', 'egovfinancials', TO_DATE('03/17/2008 11:59:39', 'MM/DD/YYYY HH24:MI:SS'), 'N', 1, TO_DATE('01/01/2006 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('01/01/2011 00:00:00', 'MM/DD/YYYY HH24:MI:SS'));
INSERT INTO EG_USER
   (ID_USER, FIRST_NAME, USER_NAME, PWD, PWD_REMINDER, UPDATETIME, IS_SUSPENDED, ISACTIVE, FROMDATE, TODATE)
 VALUES
   (seq_eg_user.NEXTVAL, 'Cao1', 'Cao1', 't27o223b7q3k0mtic20k1u32n', 'egovfinancials', TO_DATE('03/17/2008 11:59:39', 'MM/DD/YYYY HH24:MI:SS'), 'N', 1, TO_DATE('01/01/2006 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('01/01/2011 00:00:00', 'MM/DD/YYYY HH24:MI:SS'));



INSERT INTO EG_EMPLOYEE
   (ID, CODE, DATE_OF_BIRTH, BLOOD_GROUP, MOTHER_TONUGE, RELIGION_ID, COMMUNITY_ID, GENDER, IS_HANDICAPPED, IS_MED_REPORT_AVAILABLE, DATE_OF_FIRST_APPOINTMENT, IDENTIFICATION_MARKS1, LANGUAGES_KNOWN_ID, MODE_OF_RECRUIMENT_ID, RECRUITMENT_TYPE_ID, CATEGORY_ID, QULIFIED_ID, SALARY_BANK, BANK,  PAY_FIXED_IN_ID, GRADE_ID, PRESENT_DESIGNATION, SCALE_OF_PAY, BASIC_PAY, SPL_PAY, PP_SGPP_PAY, ANNUAL_INCREMENT_ID, GPF_AC_NUMBER, RETIREMENT_AGE, PRESENT_DEPARTMENT, IF_ON_DUTY_ARRANGMENT_DUTY_DEP, LOCATION, COST_CENTER, ID_DEPT, ID_USER, ISACTIVE, EMPFATHER_FIRSTNAME, EMPFATHER_LASTNAME, EMPFATHER_MIDDLENAME, EMP_FIRSTNAME, EMP_LASTNAME, EMP_MIDDLENAME, IDENTIFICATION_MARKS2, PAN_NUMBER, NAME, MATURITY_DATE,EMPCATMSTR_ID)
 VALUES
   (EGPIMS_PERSONAL_INFO_SEQ.NEXTVAL, 230, TO_DATE('11/11/1982 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), NULL, NULL, 
    NULL, NULL, '0', '0', '0', 
    TO_DATE('11/11/2007 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), NULL, NULL, 1, 1, 
    NULL, NULL, NULL, NULL, NULL, 
    NULL, NULL, NULL,  NULL, 
    NULL, NULL, NULL,  '452454', 
    NULL, NULL, NULL, NULL, NULL, 
    NULL, (SELECT id_user FROM EG_USER WHERE user_name ='Cleark1'), 1, NULL, NULL, 
    NULL, 'Cleark1', NULL, NULL, NULL, 
    NULL, 'Cleark1', NULL,3);
INSERT INTO EG_EMPLOYEE
   (ID, CODE, DATE_OF_BIRTH, BLOOD_GROUP, MOTHER_TONUGE, RELIGION_ID, COMMUNITY_ID, GENDER, IS_HANDICAPPED, IS_MED_REPORT_AVAILABLE, DATE_OF_FIRST_APPOINTMENT, IDENTIFICATION_MARKS1, LANGUAGES_KNOWN_ID, MODE_OF_RECRUIMENT_ID, RECRUITMENT_TYPE_ID, CATEGORY_ID, QULIFIED_ID, SALARY_BANK, BANK,  PAY_FIXED_IN_ID, GRADE_ID, PRESENT_DESIGNATION, SCALE_OF_PAY, BASIC_PAY, SPL_PAY, PP_SGPP_PAY, ANNUAL_INCREMENT_ID, GPF_AC_NUMBER, RETIREMENT_AGE, PRESENT_DEPARTMENT, IF_ON_DUTY_ARRANGMENT_DUTY_DEP, LOCATION, COST_CENTER, ID_DEPT, ID_USER, ISACTIVE, EMPFATHER_FIRSTNAME, EMPFATHER_LASTNAME, EMPFATHER_MIDDLENAME, EMP_FIRSTNAME, EMP_LASTNAME, EMP_MIDDLENAME, IDENTIFICATION_MARKS2, PAN_NUMBER, NAME, MATURITY_DATE,EMPCATMSTR_ID)
 VALUES
   (EGPIMS_PERSONAL_INFO_SEQ.NEXTVAL, '235', TO_DATE('11/11/1982 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), NULL, NULL, 
    NULL, NULL, '0', '0', '0', 
    TO_DATE('11/11/2007 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), NULL, NULL, 1, 1, 
    NULL, NULL, NULL, NULL, NULL, 
    NULL, NULL, NULL, NULL, NULL, 
    NULL, NULL,  '452454', 
    NULL, NULL, NULL, NULL, NULL, 
    NULL, (SELECT id_user FROM EG_USER WHERE user_name ='Hod1'), 1, NULL, NULL, 
    NULL, 'Hod1', NULL, NULL, NULL, 
    NULL, 'Hod1', NULL,3); 
INSERT INTO EG_EMPLOYEE
   (ID, CODE, DATE_OF_BIRTH, BLOOD_GROUP, MOTHER_TONUGE, RELIGION_ID, COMMUNITY_ID, GENDER, IS_HANDICAPPED, IS_MED_REPORT_AVAILABLE, DATE_OF_FIRST_APPOINTMENT, IDENTIFICATION_MARKS1, LANGUAGES_KNOWN_ID, MODE_OF_RECRUIMENT_ID, RECRUITMENT_TYPE_ID, CATEGORY_ID, QULIFIED_ID, SALARY_BANK, BANK, PAY_FIXED_IN_ID, GRADE_ID, PRESENT_DESIGNATION, SCALE_OF_PAY, BASIC_PAY, SPL_PAY, PP_SGPP_PAY, ANNUAL_INCREMENT_ID, GPF_AC_NUMBER, RETIREMENT_AGE, PRESENT_DEPARTMENT, IF_ON_DUTY_ARRANGMENT_DUTY_DEP, LOCATION, COST_CENTER, ID_DEPT, ID_USER, ISACTIVE, EMPFATHER_FIRSTNAME, EMPFATHER_LASTNAME, EMPFATHER_MIDDLENAME, EMP_FIRSTNAME, EMP_LASTNAME, EMP_MIDDLENAME, IDENTIFICATION_MARKS2, PAN_NUMBER, NAME, MATURITY_DATE,EMPCATMSTR_ID)
 VALUES
   (EGPIMS_PERSONAL_INFO_SEQ.NEXTVAL, '240', TO_DATE('11/11/1982 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), NULL, NULL, 
    NULL, NULL, '0', '0', '0', 
    TO_DATE('11/11/2007 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), NULL, NULL, 1, 1, 
    NULL, NULL, NULL, NULL, NULL, 
    NULL, NULL, NULL, NULL, NULL, 
    NULL, NULL,  '452454', 
    NULL, NULL, NULL, NULL, NULL, 
    NULL, (SELECT id_user FROM EG_USER WHERE user_name ='Accountant1'), 1, NULL, NULL, 
    NULL, 'Accountant1', NULL, NULL, NULL, 
    NULL, 'Accountant1', NULL,3); 
INSERT INTO EG_EMPLOYEE
   (ID, CODE, DATE_OF_BIRTH, BLOOD_GROUP, MOTHER_TONUGE, RELIGION_ID, COMMUNITY_ID, GENDER, IS_HANDICAPPED, IS_MED_REPORT_AVAILABLE, DATE_OF_FIRST_APPOINTMENT, IDENTIFICATION_MARKS1, LANGUAGES_KNOWN_ID, MODE_OF_RECRUIMENT_ID, RECRUITMENT_TYPE_ID,  CATEGORY_ID, QULIFIED_ID, SALARY_BANK, BANK,  PAY_FIXED_IN_ID, GRADE_ID, PRESENT_DESIGNATION, SCALE_OF_PAY, BASIC_PAY, SPL_PAY, PP_SGPP_PAY, ANNUAL_INCREMENT_ID, GPF_AC_NUMBER, RETIREMENT_AGE, PRESENT_DEPARTMENT, IF_ON_DUTY_ARRANGMENT_DUTY_DEP, LOCATION, COST_CENTER, ID_DEPT, ID_USER, ISACTIVE, EMPFATHER_FIRSTNAME, EMPFATHER_LASTNAME, EMPFATHER_MIDDLENAME, EMP_FIRSTNAME, EMP_LASTNAME, EMP_MIDDLENAME, IDENTIFICATION_MARKS2, PAN_NUMBER, NAME, MATURITY_DATE,EMPCATMSTR_ID)
 VALUES
   (EGPIMS_PERSONAL_INFO_SEQ.NEXTVAL, '245', TO_DATE('11/11/1982 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), NULL, NULL, 
    NULL, NULL, '0', '0', '0', 
    TO_DATE('11/11/2007 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), NULL, NULL, 1, 1, 
    NULL, NULL, NULL, NULL, NULL, 
    NULL, NULL, NULL, NULL, NULL, 
    NULL, NULL,  '452454', 
    NULL, NULL, NULL, NULL, NULL, 
    NULL, (SELECT id_user FROM EG_USER WHERE user_name ='Cao1'), 1, NULL, NULL, 
    NULL, 'DC', NULL, NULL, NULL, 
    NULL, 'Cao1', NULL,3);
 


INSERT INTO EG_EMP_ASSIGNMENT_PRD
   (ID, FROM_DATE, TO_DATE, ID_EMPLOYEE)
 VALUES
   (seq_ass_prd.NEXTVAL, TO_DATE('01/01/1990 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), NULL, (SELECT id FROM EG_EMPLOYEE WHERE name='Cleark1'));
INSERT INTO EG_EMP_ASSIGNMENT_PRD
   (ID, FROM_DATE, TO_DATE, ID_EMPLOYEE)
 VALUES
   (seq_ass_prd.NEXTVAL, TO_DATE('01/01/1990 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), NULL, (SELECT id FROM EG_EMPLOYEE WHERE name='Hod1'));

   INSERT INTO EG_EMP_ASSIGNMENT_PRD
   (ID, FROM_DATE, TO_DATE, ID_EMPLOYEE)
 VALUES
   (seq_ass_prd.NEXTVAL, TO_DATE('01/01/1990 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), NULL, (SELECT id FROM EG_EMPLOYEE WHERE name='Accountant1'));

INSERT INTO EG_EMP_ASSIGNMENT_PRD
   (ID, FROM_DATE, TO_DATE, ID_EMPLOYEE)
 VALUES
   (seq_ass_prd.NEXTVAL, TO_DATE('01/01/1990 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), NULL, (SELECT id FROM EG_EMPLOYEE WHERE name='Cao1'));



 INSERT INTO EG_EMP_ASSIGNMENT
   (ID, ID_FUND, ID_FUNCTION, DESIGNATIONID, ID_FUNCTIONARY, PCT_ALLOCATION, REPORTS_TO, ID_EMP_ASSIGN_PRD, FIELD, MAIN_DEPT, POSITION_ID,GOVT_ORDER_NO, GRADE_ID)
 VALUES
   (seq_ass.NEXTVAL, 12, 1, (SELECT designationid FROM EG_DESIGNATION WHERE designation_name='CLEARK' ), 
    NULL, NULL, (SELECT id FROM EG_EMPLOYEE WHERE name='Hod1'),(SELECT id FROM EG_EMP_ASSIGNMENT_PRD WHERE id_employee =(SELECT id FROM EG_EMPLOYEE WHERE name='Cleark1')), NULL, 
    (select id_dept from eg_department where dept_name ='H-Health'), (SELECT id FROM EG_POSITION WHERE position_name='Cleark_1'),NULL,NULL);

 INSERT INTO EG_EMP_ASSIGNMENT
   (ID, ID_FUND, ID_FUNCTION, DESIGNATIONID, ID_FUNCTIONARY, PCT_ALLOCATION, REPORTS_TO, ID_EMP_ASSIGN_PRD, FIELD, MAIN_DEPT, POSITION_ID,GOVT_ORDER_NO, GRADE_ID)
 VALUES
   (seq_ass.NEXTVAL, 12, 1, (SELECT designationid FROM EG_DESIGNATION WHERE designation_name='HOD1' ), 
    NULL, NULL, (SELECT id FROM EG_EMPLOYEE WHERE name='Accountant1'),(SELECT id FROM EG_EMP_ASSIGNMENT_PRD WHERE id_employee =(SELECT id FROM EG_EMPLOYEE WHERE name='Hod1')), NULL, 
   (select id_dept from eg_department where dept_name ='H-Health'),( SELECT id FROM EG_POSITION WHERE position_name='Hod_1'),NULL,NULL);

 INSERT INTO EG_EMP_ASSIGNMENT 
   (ID, ID_FUND, ID_FUNCTION, DESIGNATIONID, ID_FUNCTIONARY, PCT_ALLOCATION, REPORTS_TO, ID_EMP_ASSIGN_PRD, FIELD, MAIN_DEPT, POSITION_ID,GOVT_ORDER_NO, GRADE_ID)
 VALUES
   (seq_ass.NEXTVAL, 12, 1, (SELECT designationid FROM EG_DESIGNATION WHERE designation_name='ACCOUNTANT' ), 
    NULL, NULL, (SELECT id FROM EG_EMPLOYEE WHERE name='Cao1'),(SELECT id FROM EG_EMP_ASSIGNMENT_PRD WHERE id_employee =(SELECT id FROM EG_EMPLOYEE WHERE name='Accountant1')), NULL, 
   (select id_dept from eg_department where dept_name ='H-Health'),( SELECT id FROM EG_POSITION WHERE position_name='Accountant_1'),NULL,NULL);

INSERT INTO EG_EMP_ASSIGNMENT
   (ID, ID_FUND, ID_FUNCTION, DESIGNATIONID, ID_FUNCTIONARY, PCT_ALLOCATION, REPORTS_TO, ID_EMP_ASSIGN_PRD, FIELD, MAIN_DEPT, POSITION_ID,GOVT_ORDER_NO, GRADE_ID)
 VALUES
   (seq_ass.NEXTVAL, 12, 1, (SELECT designationid FROM EG_DESIGNATION WHERE designation_name='CAO' ), 
    NULL, NULL, (SELECT id FROM EG_EMPLOYEE WHERE name='Hod1'),(SELECT id FROM EG_EMP_ASSIGNMENT_PRD WHERE id_employee =(SELECT id FROM EG_EMPLOYEE WHERE name='Cao1')), NULL, 
    (select id_dept from eg_department where dept_name ='H-Health'), (SELECT id FROM EG_POSITION WHERE position_name='Cao_1'),NULL,NULL);
    



Insert into eg_user_jurlevel
   (ID_USER_JURLEVEL, ID_USER, ID_BNDRY_TYPE, UPDATETIME)
 Values
   (SEQ_EG_USER_JURLEVEL.nextVal, (select id_user from eg_user where first_name='Cleark1'), 1, TO_DATE('08/11/2009 00:00:00', 'MM/DD/YYYY HH24:MI:SS'));

Insert into eg_user_jurvalues
   (ID_USER_JURLEVEL, ID_BNDRY, ID, FROMDATE, TODATE, IS_HISTORY)
 Values
   ((select id_user_jurlevel from eg_user_jurlevel where id_user=(select id_user from eg_user where first_name='Cleark1')), 1, SEQ_EG_USER_JURVALUES.nextVal, TO_DATE('08/11/2009 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('08/11/2099 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 'N');
 
						  
						  
INSERT INTO EG_USER_JURLEVEL
   (ID_USER_JURLEVEL, ID_USER, ID_BNDRY_TYPE, UPDATETIME)
 VALUES
   (SEQ_EG_USER_JURLEVEL.NEXTVAL, (SELECT id_user FROM EG_USER WHERE first_name='Hod1'), 1, TO_DATE('08/11/2009 00:00:00', 'MM/DD/YYYY HH24:MI:SS'));

INSERT INTO EG_USER_JURVALUES
   (ID_USER_JURLEVEL, ID_BNDRY, ID, FROMDATE, TODATE, IS_HISTORY)
 VALUES
   ((SELECT id_user_jurlevel FROM EG_USER_JURLEVEL WHERE id_user=(SELECT id_user FROM EG_USER WHERE first_name='Hod1')), 1, SEQ_EG_USER_JURVALUES.NEXTVAL, TO_DATE('08/11/2009 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('08/11/2099 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 'N');

  INSERT INTO EG_USER_JURLEVEL
   (ID_USER_JURLEVEL, ID_USER, ID_BNDRY_TYPE, UPDATETIME)
 VALUES
   (SEQ_EG_USER_JURLEVEL.NEXTVAL, (SELECT id_user FROM EG_USER WHERE first_name='Accountant1'), 1, TO_DATE('08/11/2009 00:00:00', 'MM/DD/YYYY HH24:MI:SS'));

INSERT INTO EG_USER_JURVALUES
   (ID_USER_JURLEVEL, ID_BNDRY, ID, FROMDATE, TODATE, IS_HISTORY)
 VALUES
   ((SELECT id_user_jurlevel FROM EG_USER_JURLEVEL WHERE id_user=(SELECT id_user FROM EG_USER WHERE first_name='Accountant1')), 1, SEQ_EG_USER_JURVALUES.NEXTVAL, TO_DATE('08/11/2009 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('08/11/2099 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 'N');

 INSERT INTO EG_USER_JURLEVEL
   (ID_USER_JURLEVEL, ID_USER, ID_BNDRY_TYPE, UPDATETIME)
 VALUES
   (SEQ_EG_USER_JURLEVEL.NEXTVAL, (SELECT id_user FROM EG_USER WHERE first_name='Cao1'), 1, TO_DATE('08/11/2009 00:00:00', 'MM/DD/YYYY HH24:MI:SS'));

INSERT INTO EG_USER_JURVALUES
   (ID_USER_JURLEVEL, ID_BNDRY, ID, FROMDATE, TODATE, IS_HISTORY)
 VALUES
   ((SELECT id_user_jurlevel FROM EG_USER_JURLEVEL WHERE id_user=(SELECT id_user FROM EG_USER WHERE first_name='Cao1')), 1, SEQ_EG_USER_JURVALUES.NEXTVAL, TO_DATE('08/11/2009 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('08/11/2099 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 'N');



INSERT INTO eg_userrole
            (id_role, id_user, ID,
             fromdate, todate, is_history
            )
     VALUES ((SELECT id_role FROM eg_roles WHERE ROLE_NAME='SuperUser'), (SELECT id_user
                   FROM eg_user
                  WHERE user_name = 'Cleark1'), seq_eg_userrole.NEXTVAL,
             TO_DATE('01-01-2000','mm-dd-yyyy'), '', 'N'
            )   ;
	    INSERT INTO eg_userrole
            (id_role, id_user, ID,
             fromdate, todate, is_history
            )
     VALUES ((SELECT id_role FROM eg_roles WHERE ROLE_NAME='SuperUser'), (SELECT id_user
                   FROM eg_user
                  WHERE user_name = 'Hod1'), seq_eg_userrole.NEXTVAL,
             TO_DATE('01-01-2000','mm-dd-yyyy'), '', 'N'
            )   ;

	    INSERT INTO eg_userrole
            (id_role, id_user, ID,
             fromdate, todate, is_history
            )
     VALUES ((SELECT id_role FROM eg_roles WHERE ROLE_NAME='SuperUser'), (SELECT id_user
                   FROM eg_user
                  WHERE user_name = 'Accountant1'), seq_eg_userrole.NEXTVAL,
             TO_DATE('01-01-2000','mm-dd-yyyy'), '', 'N'
            )   ;

	    INSERT INTO eg_userrole
            (id_role, id_user, ID,
             fromdate, todate, is_history
            )
     VALUES ((SELECT id_role FROM eg_roles WHERE ROLE_NAME='SuperUser'), (SELECT id_user
                   FROM eg_user
                  WHERE user_name = 'Cao1'), seq_eg_userrole.NEXTVAL,
             TO_DATE('01-01-2000','mm-dd-yyyy'), '', 'N'
            )   ;

	    




#DOWn
