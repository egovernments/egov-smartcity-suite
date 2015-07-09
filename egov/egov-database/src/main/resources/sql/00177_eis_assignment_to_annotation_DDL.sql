DROP SEQUENCE SEQ_ASS;
CREATE SEQUENCE seq_egeis_assignment;

ALTER TABLE eg_emp_assignment RENAME TO egeis_assignment;
ALTER TABLE egeis_assignment RENAME COLUMN id_fund TO fund;
ALTER TABLE egeis_assignment RENAME COLUMN id_function TO "function";
ALTER TABLE egeis_assignment RENAME COLUMN id_functionary TO functionary;
ALTER TABLE egeis_assignment RENAME COLUMN designationid TO designation;
ALTER TABLE egeis_assignment RENAME COLUMN main_dept TO department;
ALTER TABLE egeis_assignment RENAME COLUMN position_id TO "position";
ALTER TABLE egeis_assignment RENAME COLUMN grade_id TO grade;
ALTER TABLE egeis_assignment RENAME COLUMN id_employee TO oldemployee;
ALTER TABLE egeis_assignment RENAME COLUMN from_date TO fromdate;
ALTER TABLE egeis_assignment RENAME COLUMN to_date TO todate;
ALTER TABLE egeis_assignment ADD COLUMN version BIGINT;
ALTER TABLE egeis_assignment ADD COLUMN employee BIGINT;
ALTER TABLE egeis_assignment DROP COLUMN pct_allocation;
ALTER TABLE egeis_assignment DROP COLUMN reports_to;
ALTER TABLE egeis_assignment DROP COLUMN id_emp_assign_prd;
ALTER TABLE egeis_assignment DROP COLUMN field;
ALTER TABLE egeis_assignment DROP COLUMN govt_order_no;
ALTER TABLE egeis_assignment DROP COLUMN is_primary CASCADE;
ALTER TABLE egeis_assignment ADD COLUMN isprimary BOOLEAN;
ALTER TABLE egeis_assignment RENAME COLUMN modifieddate TO lastmodifieddate;
ALTER TABLE egeis_assignment RENAME COLUMN modifiedby TO lastmodifiedby;
ALTER TABLE egeis_assignment ALTER COLUMN lastmodifiedby TYPE BIGINT;
ALTER TABLE egeis_assignment ALTER COLUMN createdby TYPE BIGINT;
ALTER TABLE egeis_assignment ALTER COLUMN createddate TYPE timestamp without time zone;
ALTER TABLE egeis_assignment ALTER COLUMN lastmodifieddate TYPE timestamp without time zone;
UPDATE egeis_assignment SET lastmodifiedby=1, createdby=1, createddate='01-01-2015',lastmodifieddate='01-01-2015',version=0;

CREATE OR REPLACE VIEW EG_EIS_EMPLOYEEINFO("ASS_ID","ID", "CODE", "NAME", "DESIGNATIONID",
 "FROM_DATE", "TO_DATE", "DATE_OF_FA", "ISACTIVE", "DEPT_ID", "FUNCTIONARY_ID", "POS_ID", "USER_ID", "STATUS", "EMPLOYEE_TYPE",
 "IS_PRIMARY","ASS_ID_UNIQUE","FUNCTION_ID","USER_ACTIVE","USER_NAME") AS 
  SELECT EEA.ID,EE.ID,EE.CODE,EE.EMP_FIRSTNAME||' '||EE.EMP_MIDDLENAME||' '||EE.EMP_LASTNAME,     
 EEA.DESIGNATION,EEA.FROMDATE,EEA.TODATE,EE.DATE_OF_FIRST_APPOINTMENT,EE.ISACTIVE,      
 EEA.DEPARTMENT,EEA.FUNCTIONARY,EEA.POSITION,ee.ID_USER,ee.STATUS ,ee.EMPLOYMENT_STATUS ,EEA.ISPRIMARY,EEA.ID,EEA.FUNCTION,
 EU.ACTIVE,EU.USERNAME 
FROM EGEIS_ASSIGNMENT EEA, EG_EMPLOYEE EE,EG_USER EU  WHERE EE.ID = EEA.OLDEMPLOYEE AND EU.ID = EE.ID_USER;

--rollback CREATE OR REPLACE VIEW EG_EIS_EMPLOYEEINFO("ASS_ID","ID", "CODE", "NAME", "DESIGNATIONID",
-- "FROM_DATE", "TO_DATE", "DATE_OF_FA", "ISACTIVE", "DEPT_ID", "FUNCTIONARY_ID", "POS_ID", "USER_ID", "STATUS", "EMPLOYEE_TYPE",
-- "IS_PRIMARY","ASS_ID_UNIQUE","FUNCTION_ID","USER_ACTIVE","USER_NAME") AS 
-- SELECT EEA.ID,EE.ID,EE.CODE,EE.EMP_FIRSTNAME||' '||EE.EMP_MIDDLENAME||' '||EE.EMP_LASTNAME,     
-- EEA.DESIGNATIONID,EEA.FROM_DATE,EEA.TO_DATE,EE.DATE_OF_FIRST_APPOINTMENT,EE.ISACTIVE,      
-- EEA.MAIN_DEPT,EEA.ID_FUNCTIONARY,EEA.POSITION_ID,ee.ID_USER,ee.STATUS ,ee.EMPLOYMENT_STATUS ,EEA.IS_PRIMARY,EEA.ID,EEA.ID_FUNCTION,
-- EU.ISACTIVE,EU.USERNAME 
--FROM EG_EMP_ASSIGNMENT EEA, EG_EMPLOYEE EE,EG_USER EU  WHERE EE.ID = EEA.ID_EMPLOYEE AND EU.ID = EE.ID_USER;

--rollback DROP SEQUENCE seq_egeis_assignment;
--rollback CREATE SEQUENCE seq_ass;

--rollback ALTER TABLE egeis_assignment RENAME TO eg_emp_assignment;
--rollback ALTER TABLE eg_emp_assignment RENAME COLUMN fund TO id_fund;
--rollback ALTER TABLE eg_emp_assignment RENAME COLUMN "function" TO id_function;
--rollback ALTER TABLE eg_emp_assignment RENAME COLUMN designation TO designationid;
--rollback ALTER TABLE eg_emp_assignment RENAME COLUMN department TO main_dept;
--rollback ALTER TABLE eg_emp_assignment RENAME COLUMN "position" TO position_id;
--rollback ALTER TABLE eg_emp_assignment RENAME COLUMN grade TO grade_id;
--rollback ALTER TABLE eg_emp_assignment RENAME COLUMN oldemployee TO id_employee;
--rollback ALTER TABLE eg_emp_assignment RENAME COLUMN fromdate TO from_date;
--rollback ALTER TABLE eg_emp_assignment RENAME COLUMN todate TO to_date;
--rollback ALTER TABLE eg_emp_assignment DROP COLUMN employee;
--rollback ALTER TABLE eg_emp_assignment DROP COLUMN isprimary;
--rollback ALTER TABLE eg_emp_assignment ADD COLUMN pct_allocation VARCHAR(256);
--rollback ALTER TABLE eg_emp_assignment reports_to BIGINT;
--rollback ALTER TABLE eg_emp_assignment field BIGINT;
--rollback ALTER TABLE eg_emp_assignment govt_order_no VARCHAR(256);
--rollbakc ALTER TABLE eg_emp_assignment is_primary CHAR(1);

--rollback ALTER TABLE eg_emp_assignment DROP COLUMN version;
--rollback ALTER TABLE eg_emp_assignment DROP COLUMN createddate;
--rollback ALTER TABLE eg_emp_assignment DROP COLUMN lastmodifieddate;
--rollback ALTER TABLE eg_emp_assignment DROP COLUMN createdby;
--rollback ALTER TABLE eg_emp_assignment DROP COLUMN lastmodifiedby;





