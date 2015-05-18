-- DESIGNATION-- 

ALTER TABLE eg_designation DROP COLUMN deptid;
ALTER TABLE eg_designation DROP COLUMN designation_local;
ALTER TABLE eg_designation DROP COLUMN officer_level;
ALTER TABLE eg_designation DROP COLUMN sanctioned_posts;
ALTER TABLE eg_designation DROP COLUMN outsourced_posts;
ALTER TABLE eg_designation DROP COLUMN basic_from;
ALTER TABLE eg_designation DROP COLUMN basic_to;
ALTER TABLE eg_designation DROP COLUMN ann_increment;
ALTER TABLE eg_designation DROP COLUMN reportsto;
ALTER TABLE eg_designation DROP COLUMN grade_id;
ALTER TABLE eg_designation ADD CONSTRAINT FK_DESINATOIN_GLCODEID FOREIGN KEY (glcodeid)
REFERENCES chartofaccounts(id);

-- DEPARTMENT-- 

ALTER TABLE eg_department DROP COLUMN dept_details;
ALTER TABLE eg_department DROP COLUMN dept_addr;
ALTER TABLE eg_department DROP COLUMN isbillinglocation;
ALTER TABLE eg_department DROP COLUMN parentid;
ALTER TABLE eg_department DROP COLUMN isleaf;

-- POSITION-- 

ALTER TABLE eg_position DROP COLUMN sanctioned_posts;
ALTER TABLE eg_position DROP COLUMN outsourced_posts;
ALTER TABLE eg_position DROP COLUMN desig_id;
ALTER TABLE eg_position DROP COLUMN effective_date;

ALTER TABLE eg_position ADD COLUMN modifiedby INTEGER;
ALTER TABLE eg_position ADD COLUMN modifieddate DATE;
ALTER TABLE eg_position ADD COLUMN createddate DATE;
ALTER TABLE eg_position ADD COLUMN createdby INTEGER;
ALTER TABLE eg_position ADD CONSTRAINT FK_POSITION_CREATEDBY FOREIGN KEY (createdby)
REFERENCES eg_user(id_user);
ALTER TABLE eg_position ADD CONSTRAINT FK_POSITION_MODIFIEDBY FOREIGN KEY (modifiedby)
REFERENCES eg_user(id_user);

-- ASSIGNMENT-- 

DROP VIEW eg_eis_employeeinfo;
DROP VIEW v_egeis_empdetails;
ALTER TABLE eg_emp_assignment ADD COLUMN modifiedby INTEGER;
ALTER TABLE eg_emp_assignment ADD COLUMN modifieddate DATE;
ALTER TABLE eg_emp_assignment ADD COLUMN createddate DATE;
ALTER TABLE eg_emp_assignment ADD COLUMN createdby INTEGER;
ALTER TABLE eg_emp_assignment ADD COLUMN id_employee INTEGER;
ALTER TABLE eg_emp_assignment ADD COLUMN from_date DATE;
ALTER TABLE eg_emp_assignment ADD COLUMN to_date DATE;
ALTER TABLE eg_emp_assignment ADD CONSTRAINT FK_ASSIGNMENT_CREATEDBY FOREIGN KEY (createdby)
REFERENCES eg_user(id_user);
ALTER TABLE eg_emp_assignment ADD CONSTRAINT FK_ASSIGNMENT_MODIFIEDBY FOREIGN KEY (modifiedby)
REFERENCES eg_user(id_user);
ALTER TABLE eg_emp_assignment ADD CONSTRAINT FK_ASSIGNMENT_EMPLOYEEID FOREIGN KEY (id_employee)
REFERENCES eg_employee(id);

-- EMPLOYEEVIEW-- 

 CREATE OR REPLACE VIEW EG_EIS_EMPLOYEEINFO("ASS_ID","ID", "CODE", "NAME", "DESIGNATIONID",
 "FROM_DATE", "TO_DATE", "DATE_OF_FA", "ISACTIVE", "DEPT_ID", "FUNCTIONARY_ID", "POS_ID", "USER_ID", "STATUS", "EMPLOYEE_TYPE",
 "IS_PRIMARY","ASS_ID_UNIQUE","FUNCTION_ID","USER_ACTIVE","USER_NAME") AS 
  SELECT EEA.ID,EE.ID,EE.CODE,EE.EMP_FIRSTNAME||' '||EE.EMP_MIDDLENAME||' '||EE.EMP_LASTNAME,     
 EEA.DESIGNATIONID,EEA.FROM_DATE,EEA.TO_DATE,EE.DATE_OF_FIRST_APPOINTMENT,EE.ISACTIVE,      
 EEA.MAIN_DEPT,EEA.ID_FUNCTIONARY,EEA.POSITION_ID,ee.ID_USER,ee.STATUS ,ee.EMPLOYMENT_STATUS ,EEA.IS_PRIMARY,EEA.ID,EEA.ID_FUNCTION,
 EU.ISACTIVE,EU.USER_NAME 
FROM EG_EMP_ASSIGNMENT EEA, EG_EMPLOYEE EE,EG_USER EU  WHERE EE.ID = EEA.ID_EMPLOYEE AND EU.ID_USER = EE.ID_USER;

-- FOR STORES MODULE(414_Stores_DTS_ViewQueries.sql)-- 

CREATE OR REPLACE VIEW V_EGEIS_EMPDETAILS
(EMPCODE, EMPLOYEENAME, DESIGNATION, EMPLOYEEASSIGNFROMDATE, EMPLOYEEASSIGNTODATE, 
 DEPARTMENT, SECTION)
AS 
SELECT EE.CODE as EmpCode,EE.EMP_FIRSTNAME as EmployeeName,     
 desig.DESIGNATION_NAME as Designation,EEA.FROM_DATE as EmployeeAssignFromDate,EEA.TO_DATE as EmployeeAssignToDate,     
 dept.DEPT_NAME as Department,func.name as Section  FROM   
 EG_EMP_ASSIGNMENT EEA, EG_EMPLOYEE EE,eg_designation desig,eg_department dept,functionary func
   WHERE EE.ID = EEA.ID_EMPLOYEE and eea.DESIGNATIONID=desig.DESIGNATIONID and EE.ISACTIVE=1 and 
   EEA.MAIN_DEPT=dept.id_dept and func.id=EEA.ID_FUNCTIONARY;

-- EGEIS_GRADE_MSTR;
ALTER TABLE egeis_grade_mstr ADD COLUMN order_no INTEGER;

-- EGEIS_CATEGORY_MSTR;
ALTER TABLE egeis_category_mstr ADD COLUMN fileid BIGINT;

-- EG_EMPLOYEE;
ALTER TABLE eg_employee ADD COLUMN permanent_address VARCHAR(1024);
ALTER TABLE eg_employee ADD COLUMN correspondence_address VARCHAR(1024);


--rollback ALTER TABLE eg_designation ADD COLUMN deptid INTEGER;
--rollback ALTER TABLE eg_designation ADD COLUMN designation_local VARCHAR(32);
--rollback ALTER TABLE eg_designation ADD COLUMN officer_level INTEGER;
--rollback ALTER TABLE eg_designation ADD COLUMN sanctioned_posts INTEGER;
--rollback ALTER TABLE eg_designation ADD COLUMN outsourced_posts INTEGER;
--rollback ALTER TABLE eg_designation ADD COLUMN basic_from INTEGER;
--rollback ALTER TABLE eg_designation ADD COLUMN basic_to INTEGER;
--rollback ALTER TABLE eg_designation ADD COLUMN ann_increment INTEGER;
--rollback ALTER TABLE eg_designation ADD COLUMN reportsto INTEGER;
--rollback ALTER TABLE eg_designation ADD COLUMN grade_id INTEGER;
--rollback ALTER TABLE eg_designation DROP COLUMN glcodeid;


--rollback ALTER TABLE eg_department ADD COLUMN dept_details VARCHAR(64);
--rollback ALTER TABLE eg_department ADD COLUMN updatetime DATE;
--rollback ALTER TABLE eg_department ADD COLUMN dept_addr VARCHAR(32);
--rollback ALTER TABLE eg_department ADD COLUMN isbillinglocation INTEGER;
--rollback ALTER TABLE eg_department ADD COLUMN parentid INTEGER;
--rollback ALTER TABLE eg_department ADD COLUMN isleaf INTEGER;


--rollback ALTER TABLE eg_position ADD COLUMN sanctioned_posts INTEGER;
--rollback ALTER TABLE eg_position ADD COLUMN outsourced_posts INTEGER;
--rollback ALTER TABLE eg_position ADD COLUMN desig_id INTEGER;
--rollback ALTER TABLE eg_position ADD COLUMN effective_date INTEGER;

--rollback ALTER TABLE eg_position DROP COLUMN modifiedby;
--rollback ALTER TABLE eg_position DROP COLUMN modifieddate;
--rollback ALTER TABLE eg_position DROP COLUMN createddate;
--rollback ALTER TABLE eg_position DROP COLUMN createdby ;


--rollback ALTER TABLE eg_emp_assignment ADD COLUMN id_emp_assign_prd INTEGER;
--rollback ALTER TABLE eg_emp_assignment DROP COLUMN modifiedby;
--rollback ALTER TABLE eg_emp_assignment DROP COLUMN modifieddate;
--rollback ALTER TABLE eg_emp_assignment DROP COLUMN createddate;
--rollback ALTER TABLE eg_emp_assignment DROP COLUMN createdby;
--rollback ALTER TABLE eg_emp_assignment DROP COLUMN id_employee;
--rollback ALTER TABLE eg_emp_assignment DROP COLUMN from_date;
--rollback ALTER TABLE eg_emp_assignment DROP COLUMN to_date;
--rollback ALTER TABLE eg_emp_assignment ADD CONSTRAINT FK_ASSIGNMENT_PRDID FOREIGN KEY (id_emp_assign_prd)REFERENCES eg_emp_assignment_prd(id);

--rollback EGEIS_GRADE_MSTR;
--rollback ALTER TABLE egeis_grade_mstr DROP COLUMN order_no;

--rollback EGEIS_CATEGORY_MSTR;
--rollback ALTER TABLE egeis_category_mstr DROP COLUMN fileid;

--rollback EG_EMPLOYEE;
--rollback ALTER TABLE eg_employee DROP COLUMN permanent_address;
--rollback ALTER TABLE eg_employee DROP COLUMN correspondence_address;



--rollback CREATE OR REPLACE VIEW EG_EIS_EMPLOYEEINFO
--rollback (ASS_ID, PRD_ID, ID, CODE, NAME, 
--rollback  DESIGNATIONID, FROM_DATE, TO_DATE, REPORTS_TO, DATE_OF_FA, 
--rollback  ISACTIVE, DEPT_ID, FUNCTIONARY_ID, POS_ID, USER_ID, 
--rollback  STATUS,EMPLOYEE_TYPE,IS_PRIMARY)
--rollback AS 
--rollback SELECT EEA.ID,EAP.ID,EE.ID,EE.CODE,EE.EMP_FIRSTNAME||' '||EE.EMP_MIDDLENAME||' '||EE.EMP_LASTNAME, 
--rollback  EEA.DESIGNATIONID,EAP.FROM_DATE,EAP.TO_DATE,EEA.REPORTS_TO,EE.DATE_OF_FIRST_APPOINTMENT,EE.ISACTIVE,  
--rollback  EEA.MAIN_DEPT,EEA.ID_FUNCTIONARY,EEA.POSITION_ID,ee.ID_USER,ee.STATUS ,ee.EMPLOYMENT_STATUS ,EEA.IS_PRIMARY FROM EG_EMP_ASSIGNMENT_PRD EAP,
--rollback  EG_EMP_ASSIGNMENT EEA, EG_EMPLOYEE EE  WHERE EE.ID =
--rollback  EAP.ID_EMPLOYEE  AND EAP.ID=EEA.ID_EMP_ASSIGN_PRD;

--rollback CREATE OR REPLACE VIEW V_EGEIS_EMPDETAILS
--rollback (EMPCODE, EMPLOYEENAME, DESIGNATION, EMPLOYEEASSIGNFROMDATE, EMPLOYEEASSIGNTODATE, 
--rollback  DEPARTMENT, SECTION)
--rollback AS 
--rollback SELECT EE.CODE as EmpCode,EE.EMP_FIRSTNAME as EmployeeName,     
--rollback  desig.DESIGNATION_NAME as Designation,EAP.FROM_DATE as EmployeeAssignFromDate,EAP.TO_DATE as EmployeeAssignToDate,     
--rollback  dept.DEPT_NAME as Department,func.name as Section  FROM EG_EMP_ASSIGNMENT_PRD EAP,    
--rollback  EG_EMP_ASSIGNMENT EEA, EG_EMPLOYEE EE,eg_designation desig,eg_department dept,functionary func
--rollback    WHERE EE.ID = EAP.ID_EMPLOYEE  AND EAP.ID=EEA.ID_EMP_ASSIGN_PRD and eea.DESIGNATIONID=desig.DESIGNATIONID and EE.ISACTIVE=1 and 
--rollback    eea.MAIN_DEPT=dept.id_dept and func.id=EEA.ID_FUNCTIONARY;

