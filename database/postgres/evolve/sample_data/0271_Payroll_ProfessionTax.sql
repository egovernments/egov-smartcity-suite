#UP

 INSERT INTO EG_WF_ACTIONS ( ID, TYPE, NAME, DESCRIPTION, CREATED_BY, CREATED_DATE, MODIFIED_BY,MODIFIED_DATE ) VALUES ( 
EG_WF_ACTIONS_SEQ.NEXTVAL, 'PayHeadRule', 'PTRule', 'PTRule',NULL , sysdate, NULL, sysdate);

Insert into egpay_payhead_rule
   (ID, ID_SALARYCODE, EFFECTIVE_FROM, DESCRIPTION,ID_WF_ACTION)
 Values
   (EISPAYROLL_PAYHEAD_RULE_SEQ.nextval, (SELECT id FROM EGPAY_SALARYCODES s WHERE s.HEAD like 'PT') , TO_DATE('01/01/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 'PTRule',(select wfa.id from eg_wf_actions wfa where wfa.type='PayHeadRule' and wfa.name='PTRule'));
 

update EGPAY_SALARYCODES s set s.cal_type='RuleBased' WHERE s.HEAD ='PT';



#DOWN
