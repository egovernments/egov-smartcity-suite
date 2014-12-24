INSERT INTO EGPAY_SALARYCODES
            (ID, head, categoryid, createdby,
             createddate, lastmodifiedby,
             lastmodifieddate,
             description, is_taxable, cal_type, glcodeid, pct_basis,
             order_id, local_lang_desc, isattendancebased, isrecomputed,
             isrecurring
            )
     VALUES (eispayroll_salarycodes_seq.NEXTVAL, 'H.REN', 2, 1,
             sysdate, 1,
             sysdate,
             'HOUSE RENT', 'N', 'ComputedValue', 541, (select id from egpay_salarycodes where head='Basic'),
             5, 'H.Rent Allowance', 'Y', 'Y',
             'Y'
            );
INSERT INTO EGPAY_SALARYCODES
            (ID, head, categoryid, createdby,
             createddate, lastmodifiedby,
             lastmodifieddate,
             description, is_taxable, cal_type, glcodeid, order_id,
             local_lang_desc, isattendancebased, isrecomputed, isrecurring
            )
     VALUES (eispayroll_salarycodes_seq.NEXTVAL, 'rulePayhead', 1, 1,
            sysdate, 1,
             sysdate,
             'rulePayhead', 'N', 'RuleBased', 633, 128,
             'rulePayhead', 'N', 'N', 'Y'
            );

INSERT INTO EGPAY_EMPPAYROLL
            (ID, id_employee, id_emp_assignment, gross_pay, net_pay,
             createdby, createddate, financialyearid,
             MONTH, status, basic_pay, paytype,
             fromdate,
             todate, workingdays, numdays,
             lastmodifieddate
            )
     VALUES (eispayroll_emppayroll_seq.NEXTVAL, 
     		(select id from eg_employee where code=1), 
     		(select id from eg_emp_assignment where id_emp_assign_prd=(select id from eg_emp_assignment_prd where id_employee=(select id from eg_employee where code=1))), 
     		344, 341,
             1,sysdate, 7,
             4, (select id from egw_status where moduletype='PaySlip' and description='Created'), 2000, 1,
             TO_DATE ('04/01/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'),
             TO_DATE ('04/30/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 30, 5,
             sysdate
            );

Insert into egpay_earnings
   (ID, ID_EMPPAYROLL, ID_SALCODE, PCT, AMOUNT)
 Values
   (EISPAYROLL_EARNINGS_SEQ.nextval, 
   (select id from egpay_emppayroll where id_employee=(select id from eg_employee where code=1) and month=4 and financialyearid=7),
   (select id from egpay_salarycodes where head='DA'), 10, 5.5);
Insert into egpay_earnings
   (ID, ID_EMPPAYROLL, ID_SALCODE, AMOUNT)
 Values
   (EISPAYROLL_EARNINGS_SEQ.nextval,
   (select id from egpay_emppayroll where id_employee=(select id from eg_employee where code=1) and month=4 and financialyearid=7),
   (select id from egpay_salarycodes where head='Basic'), 333);
Insert into egpay_earnings
   (ID, ID_EMPPAYROLL, ID_SALCODE, AMOUNT)
 Values
   (EISPAYROLL_EARNINGS_SEQ.nextval, 
   (select id from egpay_emppayroll where id_employee=(select id from eg_employee where code=1) and month=4 and financialyearid=7),
   (select id from egpay_salarycodes where head='rulePayhead'), 34.4);
Insert into egpay_earnings
   (ID, ID_EMPPAYROLL, ID_SALCODE, PCT, AMOUNT)
 Values
   (EISPAYROLL_EARNINGS_SEQ.nextval, 
   (select id from egpay_emppayroll where id_employee=(select id from eg_employee where code=1) and month=4 and financialyearid=7),
   (select id from egpay_salarycodes where head='H.REN'), 10, 5.5);
COMMIT;



Insert into egpay_payhead_rule
   (ID, ID_SALARYCODE, EFFECTIVE_FROM, DESCRIPTION, RULE_FILE_PATH)
 Values
   (EISPAYROLL_PAYHEAD_RULE_SEQ.nextval, (SELECT id FROM EGPAY_SALARYCODES s WHERE s.HEAD like 'rulePayhead') , TO_DATE('01/01/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 'rulePayhead', '/org/egov/payroll/rules/rulePayhead-rule.drl');
COMMIT;


