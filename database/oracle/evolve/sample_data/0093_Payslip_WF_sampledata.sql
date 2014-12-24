#UP

Insert into eg_wf_states
   (ID, TYPE, VALUE, CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE, OWNER, TEXT1, NEXT)
 Values
   (EG_WF_STATES_SEQ.NEXTVAL, 'EmpPayroll', 'NEW', 1, TO_TIMESTAMP('13-11-2009 10:50:52.048000','DD-MM-YYYY HH24:MI:SS.FF'), 1, TO_DATE('11/13/2009 10:50:55', 'MM/DD/YYYY HH24:MI:SS'), 1, 'Created by Creator', null);

Insert into eg_wf_states
   (ID, TYPE, VALUE, CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE, OWNER, TEXT1, PREVIOUS)
 Values
   (EG_WF_STATES_SEQ.NEXTVAL, 'EmpPayroll', 'CLEARK_APPROVED', 1, TO_TIMESTAMP('13-11-2009 10:50:55.958000','DD-MM-YYYY HH24:MI:SS.FF'), 1, TO_DATE('11/13/2009 10:50:55', 'MM/DD/YYYY HH24:MI:SS'), 2, 'Created by Clerk', null);



update eg_wf_states set next=(select id from eg_wf_states where value='CLEARK_APPROVED' and type='EmpPayroll') where value='NEW' and type='EmpPayroll';
update eg_wf_states set previous=(select id from eg_wf_states where value='NEW' and type='EmpPayroll') where value='CLEARK_APPROVED' and type='EmpPayroll';




/** create payslips **/

insert into egpay_emppayroll
(ID,ID_EMPLOYEE,ID_EMP_ASSIGNMENT,GROSS_PAY,NET_PAY,CREATEDBY,CREATEDDATE,
FINANCIALYEARID,NUMDAYS,MONTH,STATUS,BASIC_PAY,PAYTYPE,WORKINGDAYS,FROMDATE,TODATE,LASTMODIFIEDDATE,STATE_ID)
values
(eispayroll_emppayroll_seq.nextval,(select id from eg_employee where code=100),
(select eea.id from eg_emp_assignment eea, eg_emp_assignment_prd eeap
     where eea.id_emp_assign_prd=eeap.id and eeap.id_employee=(select id from eg_employee where code=100)),
     10100,9300,1,sysdate,(select id from financialyear where financialyear = '2009-10'),
30,6,(select id from egw_status where moduletype='PaySlip' and description='Created'),
6400,1,30,'01-Jun-2009','30-Jun-2009',sysdate,(select id from eg_wf_states where value='CLEARK_APPROVED' and type='EmpPayroll'));

insert into egpay_earnings
(ID,ID_EMPPAYROLL,ID_SALCODE,AMOUNT)
values
(eispayroll_earnings_seq.nextval,(select id from egpay_emppayroll where id_employee=(select id from eg_employee where code=100) and month=6),
(select id from egpay_salarycodes where head = 'Basic'),6400);

insert into egpay_earnings
(ID,ID_EMPPAYROLL,ID_SALCODE,PCT,AMOUNT)
values
(eispayroll_earnings_seq.nextval,(select id from egpay_emppayroll where id_employee=(select id from eg_employee where code=100) and month=6),
(select id from egpay_salarycodes where head = 'DA'),20,1280);

insert into egpay_earnings
(ID,ID_EMPPAYROLL,ID_SALCODE,AMOUNT)
values
(eispayroll_earnings_seq.nextval,(select id from egpay_emppayroll where id_employee=(select id from eg_employee where code=100) and month=6),
(select id from egpay_salarycodes where head = 'HRA'),2300);

insert into egpay_earnings
(ID,ID_EMPPAYROLL,ID_SALCODE,AMOUNT)
values
(eispayroll_earnings_seq.nextval,(select id from egpay_emppayroll where id_employee=(select id from eg_employee where code=100) and month=6),
(select id from egpay_salarycodes where head = 'CCA'),120);

insert into egpay_deductions
(ID,ID_EMPPAYROLL,ID_SALCODE,AMOUNT)
values
(eispayroll_deductions_seq.nextval,(select id from egpay_emppayroll where id_employee=(select id from eg_employee where code=100) and month=6),
(select id from egpay_salarycodes where head = 'PF'),500);

insert into egpay_deductions
(ID,ID_EMPPAYROLL,ID_SALCODE,AMOUNT)
values
(eispayroll_deductions_seq.nextval,(select id from egpay_emppayroll where id_employee=(select id from eg_employee where code=100) and month=6),
(select id from egpay_salarycodes where head = 'PT'),200);


#DOWN

