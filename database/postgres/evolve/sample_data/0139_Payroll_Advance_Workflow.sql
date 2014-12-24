#UP

/** Sample data for position hir for advance for empcode=101**/

INSERT INTO  EG_POSITION_HIR (id,position_from,position_to,object_type_id)VALUES
	   (SEQ_POSITION_HIR.NEXTVAL,(SELECT e.POS_ID  FROM eg_eis_employeeinfo e WHERE e.CODE=101) ,(SELECT e.POS_ID  FROM eg_eis_employeeinfo e WHERE e.CODE=102),
	   (SELECT o.ID FROM EG_OBJECT_TYPE o WHERE o.TYPE LIKE'advance'));

INSERT INTO EG_WF_STATES ( ID, TYPE, VALUE, CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE,
OWNER, DATE1, DATE2, TEXT1, TEXT2, PREVIOUS, NEXT, NEXT_ACTION ) VALUES ( 
eg_wf_states_seq.nextval, 'SalaryARF', 'NEW', (SELECT e.POS_ID  FROM eg_eis_employeeinfo e WHERE e.CODE=100), TO_TIMESTAMP('13-11-2009 00:00:00.000000','DD-MM-YYYY HH24:MI:SS.FF')
,1,  TO_Date( '11/13/2009 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'),(SELECT e.POS_ID  FROM eg_eis_employeeinfo e WHERE e.CODE=101) , NULL, NULL
, 'SampleNewForSalARF', NULL, NULL, null, NULL); 

INSERT INTO EG_WF_STATES ( ID, TYPE, VALUE, CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE,
OWNER, DATE1, DATE2, TEXT1, TEXT2, PREVIOUS, NEXT, NEXT_ACTION ) VALUES ( 
eg_wf_states_seq.nextval, 'SalaryARF', 'CREATED',(SELECT e.POS_ID  FROM eg_eis_employeeinfo e WHERE e.CODE=100), TO_TIMESTAMP('13-11-2009 00:00:00.000000','DD-MM-YYYY HH24:MI:SS.FF')
, 1,  TO_Date( '11/13/2009 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'),(SELECT e.POS_ID  FROM eg_eis_employeeinfo e WHERE e.CODE=102), NULL, NULL
, 'SampleCreatedForSalARF', NULL,(Select id from eg_wf_states where type='SalaryARF' and value='NEW' and TEXT1='SampleNewForSalARF'), NULL, NULL); 

update eg_wf_states set next = (Select id from eg_wf_states where type='SalaryARF' and value='CREATED' and TEXT1='SampleCreatedForSalARF') where  type='SalaryARF' and value='NEW' and TEXT1='SampleNewForSalARF' ;

update eg_advancerequisition set STATE_ID=(Select id from eg_wf_states where type='SalaryARF' and value='CREATED' and TEXT1='SampleCreatedForSalARF') where advancerequisitionnumber='ARF/H/VEHDED/8/2010-11';

update eg_advancerequisitionmis set voucherheaderid=2 where advancerequisitionid=(select id from eg_advancerequisition where advancerequisitionnumber='ARF/H/VEHDED/8/2010-11');

/**Advance sample data for deduction-bankloan**/

INSERT INTO EG_WF_STATES ( ID, TYPE, VALUE, CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE,
OWNER, DATE1, DATE2, TEXT1, TEXT2, PREVIOUS, NEXT, NEXT_ACTION ) VALUES ( 
eg_wf_states_seq.nextval, 'Advance', 'NEW', (SELECT e.POS_ID  FROM eg_eis_employeeinfo e WHERE e.CODE=100), TO_TIMESTAMP('13-11-2009 00:00:00.000000','DD-MM-YYYY HH24:MI:SS.FF')
,1,  TO_Date( '11/13/2009 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'),(SELECT e.POS_ID  FROM eg_eis_employeeinfo e WHERE e.CODE=101) , NULL, NULL
, 'SampleNewForAdvance', NULL, NULL, null, NULL); 

INSERT INTO EG_WF_STATES ( ID, TYPE, VALUE, CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE,
OWNER, DATE1, DATE2, TEXT1, TEXT2, PREVIOUS, NEXT, NEXT_ACTION ) VALUES ( 
eg_wf_states_seq.nextval, 'Advance', 'CREATED',(SELECT e.POS_ID  FROM eg_eis_employeeinfo e WHERE e.CODE=100), TO_TIMESTAMP('13-11-2009 00:00:00.000000','DD-MM-YYYY HH24:MI:SS.FF')
, 1,  TO_Date( '11/13/2009 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'),(SELECT e.POS_ID  FROM eg_eis_employeeinfo e WHERE e.CODE=102), NULL, NULL
, 'SampleCreatedForAdvance', NULL,(Select id from eg_wf_states where type='Advance' and value='NEW' and TEXT1='SampleNewForAdvance'), NULL, NULL); 

update egpay_saladvances set state_id=(Select id from eg_wf_states where type='Advance' and value='CREATED' and TEXT1='SampleCreatedForAdvance') where id=(select id from egpay_saladvances where requested_amt='1500' and advance_amt='1500');

update eg_wf_states set next = (Select id from eg_wf_states where type='Advance' and value='CREATED' and TEXT1='SampleCreatedForAdvance') where  type='Advance' and value='NEW' and TEXT1='SampleNewForAdvance' ;

#DOWN

