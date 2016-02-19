INSERT INTO chartofaccounts (id, glcode, name, description, isactiveforposting, parentid, lastmodified, modifiedby,
 created, purposeid, operation, type, class, classification, functionreqd, budgetcheckreq, scheduleid, receiptscheduleid,
  receiptoperation, paymentscheduleid, paymentoperation, majorcode, createdby, fiescheduleid, fieoperation) 
  VALUES (nextval('seq_chartofaccounts'), '3109000', 'Excess of Income over Expenditure', NULL, true, (select id from chartofaccounts where glcode='31090'), NULL, 1, current_date, 
  (select id from egf_accountcode_purpose where name='ExcessIE'), NULL,
   'L', NULL, 4, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '310', 1, NULL, NULL);