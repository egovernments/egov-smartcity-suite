/*****01/11/2008 for gratuity disburse paidto column of MISCBILLDETAIL is nullable*****/
ALTER TABLE MISCBILLDETAIL
  MODIFY (PAIDTO   NULL);
  commit;