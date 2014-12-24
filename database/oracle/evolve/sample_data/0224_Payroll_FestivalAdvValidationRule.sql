#UP

INSERT INTO EG_WF_ACTIONS ( ID, TYPE, NAME, DESCRIPTION, CREATED_BY, CREATED_DATE, MODIFIED_BY,MODIFIED_DATE ) VALUES ( 
EG_WF_ACTIONS_SEQ.NEXTVAL, 'AdvSalarycode', 'festivalAdvRule', 'Festival advance rule', NULL, NULL, NULL, NULL); 


/*** Script for fistival advance validation rule****/

Insert into eg_script
   (ID, NAME, SCRIPT_TYPE, SCRIPT, START_DATE, END_DATE)
 Values
   (eg_script_seq.nextVal, 'Payroll.AdvSalarycode.festivalAdvRule', 'python','test', TO_DATE('01/01/1900 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('01/01/2100 00:00:00', 'MM/DD/YYYY HH24:MI:SS'));


#DOWN
