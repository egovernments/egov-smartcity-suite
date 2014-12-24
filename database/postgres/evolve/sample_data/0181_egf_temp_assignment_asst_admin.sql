#UP	
INSERT INTO EG_EMP_ASSIGNMENT (ID, ID_FUND, ID_FUNCTION, DESIGNATIONID, ID_FUNCTIONARY, PCT_ALLOCATION, REPORTS_TO, ID_EMP_ASSIGN_PRD, FIELD, MAIN_DEPT, POSITION_ID,GOVT_ORDER_NO, GRADE_ID, is_primary)
 VALUES (seq_ass.NEXTVAL,(select id from fund where code='0101'), null, (select DESIGNATIONID from eg_designation where designation_name='ASSISTANT'), 
    (select id from functionary where name='ADMIN'), NULL, NULL,(SELECT id FROM EG_EMP_ASSIGNMENT_PRD WHERE id_employee =(SELECT id FROM EG_EMPLOYEE WHERE name='ASSTADMIN')), NULL, 
    (select id_dept from eg_department where dept_code='H'), (SELECT id FROM EG_POSITION WHERE position_name='ADMIN_ASST'),NULL,NULL,'N');
 #DOWN
 
