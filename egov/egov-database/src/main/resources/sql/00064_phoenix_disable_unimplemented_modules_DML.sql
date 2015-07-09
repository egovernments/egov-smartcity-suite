
DROP VIEW eg_eis_employeeinfo;

CREATE OR REPLACE VIEW eg_eis_employeeinfo AS
 SELECT eea.id AS ass_id,
    ee.id,
    ee.code,
    (((ee.emp_firstname::text || ' '::text) || ee.emp_middlename::text) || ' '::text) || ee.emp_lastname::text AS name,
    eea.designationid,
    eea.from_date,
    eea.to_date,
    ee.date_of_first_appointment AS date_of_fa,
    ee.isactive,
    eea.main_dept AS dept_id,
    eea.id_functionary AS functionary_id,
    eea.position_id AS pos_id,
    ee.id_user AS user_id,
    ee.status,
    ee.employment_status AS employee_type,
    eea.is_primary,
    eea.id AS ass_id_unique,
    eea.id_function AS function_id,
    eu.isactive AS user_active,
    eu.name AS user_name
   FROM eg_emp_assignment eea,
    eg_employee ee,
    eg_user eu
  WHERE ee.id = eea.id_employee AND eu.id = ee.id_user; 
