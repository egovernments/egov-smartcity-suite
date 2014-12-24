#UP
INSERT INTO EG_SCRIPT (ID,NAME,SCRIPT_TYPE,SCRIPT) VALUES(EG_SCRIPT_SEQ.NEXTVAL,'egf.budget.reappropriation.sequence.generator','python',
'be_sequence="BUDGET-REAPPROPRIATION-BE" 
re_sequence="BUDGET-REAPPROPRIATION-RE" 
sequenceNumber = 0
type = wfItem.getBudget().getIsbere()
if type == "RE": 
	sequenceNumber = sequenceGenerator.getNextNumberWithFormat(re_sequence,4,"0",0).getFormattedNumber().zfill(4) 
else: 
	sequenceNumber = sequenceGenerator.getNextNumberWithFormat(be_sequence,4,"0",0).getFormattedNumber().zfill(4) 
result=type+"/"+wfItem.getBudget().getFinancialYear().getFinYearRange()+"/"+sequenceNumber');

#DOWN
delete from EG_SCRIPT where name='egf.budget.reappropriation.sequence.generator'