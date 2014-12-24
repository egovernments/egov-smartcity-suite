#UP

/** Sample data for CPFSetup. **/

insert into egpay_emppayroll
(ID,ID_EMPLOYEE,ID_EMP_ASSIGNMENT,GROSS_PAY,NET_PAY,CREATEDBY,CREATEDDATE,
FINANCIALYEARID,NUMDAYS,MONTH,STATUS,BASIC_PAY,PAYTYPE,WORKINGDAYS,FROMDATE,TODATE,LASTMODIFIEDDATE)
values
(eispayroll_emppayroll_seq.nextval,(select id from eg_employee where code=100),
(select eea.id from eg_emp_assignment eea, eg_emp_assignment_prd eeap
     where eea.id_emp_assign_prd=eeap.id and eeap.id_employee=(select id from eg_employee where code=100)),
     10100,9300,1,TO_DATE('03/31/2009 00:00:00', 'MM/DD/YYYY HH24:MI:SS'),(select id from financialyear where financialyear = '2008-09'),
28,3,(select id from egw_status where moduletype='PaySlip' and description='AuditApproved'),
6400,1,31,'01-Mar-2009','31-Mar-2009',TO_DATE('03/31/2009 00:00:00', 'MM/DD/YYYY HH24:MI:SS'));

insert into egpay_earnings
(ID,ID_EMPPAYROLL,ID_SALCODE,AMOUNT)
values
(eispayroll_earnings_seq.nextval,(select id from egpay_emppayroll where id_employee=(select id from eg_employee where code=100) and month=3),
(select id from egpay_salarycodes where head = 'Basic'),6400);

insert into egpay_earnings
(ID,ID_EMPPAYROLL,ID_SALCODE,PCT,AMOUNT)
values
(eispayroll_earnings_seq.nextval,(select id from egpay_emppayroll where id_employee=(select id from eg_employee where code=100) and month=3),
(select id from egpay_salarycodes where head = 'DA'),20,1280);

insert into egpay_deductions
(ID,ID_EMPPAYROLL,ID_SALCODE,AMOUNT)
values
(eispayroll_deductions_seq.nextval,(select id from egpay_emppayroll where id_employee=(select id from eg_employee where code=100) and month=3),
(select id from egpay_salarycodes where head = 'CPF'),500);

insert into egpay_deductions
(ID,ID_EMPPAYROLL,ID_SALCODE,AMOUNT)
values
(eispayroll_deductions_seq.nextval,(select id from egpay_emppayroll where id_employee=(select id from eg_employee where code=100) and month=3),
(select id from egpay_salarycodes where head = 'PT'),200);

COMMIT;

#DOWN





