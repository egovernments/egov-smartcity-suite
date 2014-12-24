#UP

UPDATE EGPAY_SALARYCODES 
SET CAL_TYPE='RuleBased'
WHERE UPPER(HEAD) LIKE 'DA';

INSERT INTO EGPAY_PAYHEAD_RULE (ID, ID_SALARYCODE, EFFECTIVE_FROM, DESCRIPTION, RULE_FILE_PATH) 
VALUES (EISPAYROLL_PAYHEAD_RULE_SEQ.nextval , (select id from egpay_salarycodes where upper(head) like 'DA') , to_date('01-Jan-2004'), '10% of Basic + 20% of HRA', '/org/egov/payroll/rules/DAPayhead-rule.drl');


#DOWN