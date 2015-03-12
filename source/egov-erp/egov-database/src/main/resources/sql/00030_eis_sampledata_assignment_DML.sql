UPDATE eg_emp_assignment SET is_primary='N' WHERE id IN(2,3);
UPDATE eg_emp_assignment SET id_employee=1,from_date=current_date-365,to_date=current_date+10000 WHERE id IN(1,2,3);

--rollback UPDATE eg_emp_assignment SET is_primary='Y' WHERE id IN(2,3);
--rollback UPDATE eg_emp_assignment SET id_employee=null WHERE id IN(1,2,3);
