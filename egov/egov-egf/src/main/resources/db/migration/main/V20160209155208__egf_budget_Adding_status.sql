ALTER TABLE egf_budget ADD COLUMN status bigint;

update egf_budget set status  = (select id from egw_status where moduletype = 'BUDGET' and description = 'Approved');

INSERT INTO EG_SCRIPT (ID,NAME,type,SCRIPT,version) VALUES(nextval('seq_eg_script'),'egf.reappropriation.sequence.generator','python',
'be_sequence="BUDGET-REAPPROPRIATION" 
result = "BANo:"+sequenceGenerator.getNextNumberWithFormat(be_sequence,3,"0",0).getFormattedNumber().zfill(3)+"/"+wfItem.getFinYearRange()',0);

INSERT INTO EG_NUMBER_GENERIC ( ID, OBJECTTYPE, VALUE,UPDATEDTIMESTAMP ) VALUES (nextval('SEQ_EG_NUMBER_GENERIC'), 'BUDGET-REAPPROPRIATION', 0,  current_date); 



