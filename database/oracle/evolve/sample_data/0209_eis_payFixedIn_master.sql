#UP
Insert into EGEIS_PAY_FIXED_IN_MSTR
   (PAY_FIXED_IN_ID, PAY_FIXED_IN_VALUE, START_DATE, END_DATE)
 Values
   (EGPIMS_PAY_FIXED_SEQ.nextval, 'sixPay', TO_DATE('01/01/1900 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('01/01/2100 00:00:00', 'MM/DD/YYYY HH24:MI:SS'));
   
Insert into EGEIS_PAY_FIXED_IN_MSTR
   (PAY_FIXED_IN_ID, PAY_FIXED_IN_VALUE, START_DATE, END_DATE)
 Values
   (EGPIMS_PAY_FIXED_SEQ.nextval, 'fifthPay', TO_DATE('01/01/1900 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('01/01/2100 00:00:00', 'MM/DD/YYYY HH24:MI:SS')); 

update eg_employee set pay_fixed_in_id=(select pay_fixed_in_id from egeis_pay_fixed_in_mstr where pay_fixed_in_value like'fifthPay');

#DOWN