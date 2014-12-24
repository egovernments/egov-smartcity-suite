call emp_paymasters.load_salarycodes();

call emp_paymasters.CREATE_CONTROLCODE();

call  emp_paymasters.knn_emp_payscale();

call emp_payroll.knn_emp_assignment();

call emp_payroll.knn_position_assignment();

call emp_payroll.knn_emp_roll('01-Jan-2009');

call emp_payroll.empupdate_masters();
