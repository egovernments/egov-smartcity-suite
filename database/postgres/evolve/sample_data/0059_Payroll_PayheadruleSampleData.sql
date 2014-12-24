#UP
/** Updating rule script for each payhead in payhead rule .Sample data**/

update egpay_payhead_rule phr set phr.id_wf_action=(select wfa.id from eg_wf_actions wfa where wfa.type='PayHeadRule' and wfa.name='PctBasicAttendanceY') where id_salarycode=( SELECT id FROM EGPAY_SALARYCODES s WHERE s.HEAD like 'pctOfBasicAttendanceY') and description='pctOfBasicAttendanceY';
update egpay_payhead_rule phr set phr.id_wf_action=(select wfa.id from eg_wf_actions wfa where wfa.type='PayHeadRule' and wfa.name='PctBasicGradePay') where id_salarycode=( SELECT id FROM EGPAY_SALARYCODES s WHERE s.HEAD like 'pctOfBasicGradePay') and description='pctOfBasicGradePay';
update egpay_payhead_rule phr set phr.id_wf_action=(select wfa.id from eg_wf_actions wfa where wfa.type='PayHeadRule' and wfa.name='PctBasicHRA') where id_salarycode=( SELECT id FROM EGPAY_SALARYCODES s WHERE s.HEAD like 'pctOfBasicHRA') and description='pctOfBasicHRA';
update egpay_payhead_rule phr set phr.id_wf_action=(select wfa.id from eg_wf_actions wfa where wfa.type='PayHeadRule' and wfa.name='PctBasic') where id_salarycode=( SELECT id FROM EGPAY_SALARYCODES s WHERE s.HEAD like 'pctOfBasicRule') and description='pctOfBasicRule';

Insert into egpay_payhead_rule
   (ID, ID_SALARYCODE, EFFECTIVE_FROM, DESCRIPTION,ID_WF_ACTION)
 Values
   (EISPAYROLL_PAYHEAD_RULE_SEQ.nextval, (SELECT id FROM EGPAY_SALARYCODES s WHERE s.HEAD like 'Ot') , TO_DATE('01/01/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 'OtpayRule',(select wfa.id from eg_wf_actions wfa where wfa.type='PayHeadRule' and wfa.name='OtPayHead'));
 
 
commit;

#DOWN
