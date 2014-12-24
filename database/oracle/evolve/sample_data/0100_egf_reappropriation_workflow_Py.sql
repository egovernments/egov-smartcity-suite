#UP
INSERT INTO EG_SCRIPT (ID,NAME,SCRIPT_TYPE,SCRIPT) VALUES(EG_SCRIPT_SEQ.NEXTVAL,'BudgetReAppropriationMisc.workflow','python',
'def transition_reappropriations(misc,state,position,comments):
	for r in misc.getBudgetReAppropriations():
		r.changeState(state,position,comments)
commissioner = persistenceService.find("from Position where name=''EGFCOMMISSIONER''")
if action.getName()=="submit_for_approval":
	wfItem.changeState("PENDING_APPROVAL",commissioner,comments)
	transition_reappropriations(wfItem,"PENDING_APPROVAL",commissioner,comments)
elif wfItem.getCurrentState().getOwner().getName()=="EGFCOMMISSIONER" and action.getName()=="approve" :
	wfItem.changeState("APPROVED",commissioner,comments)
	transition_reappropriations(wfItem,"APPROVED",commissioner,comments)
	transition_reappropriations(wfItem,"END",commissioner,comments)	
	wfItem.changeState("END",commissioner,comments)
elif wfItem.getCurrentState().getOwner().getName()=="EGFCOMMISSIONER" and action.getName()=="reject":
	wfItem.changeState("REJECTED",wfItem.getCurrentState().getPrevious().getOwner(),comments)
	transition_reappropriations(wfItem,"REJECTED",wfItem.getCurrentState().getPrevious().getOwner(),comments)
persistenceService.persist(wfItem)');
		
INSERT INTO EG_SCRIPT (ID,NAME,SCRIPT_TYPE,SCRIPT) VALUES(EG_SCRIPT_SEQ.NEXTVAL,'BudgetReAppropriationMisc.workflow.validactions','python',
'result = []
transitions={"default":["submit_for_approval"],"EGFCOMMISSIONER":["approve","reject"]} 
position = wfItem.getCurrentState().getOwner().getName()
if position=="EGFCOMMISSIONER":
	result = transitions[position]
else:
	result = transitions["default"]
result');
		

#DOWN
delete from EG_SCRIPT where name='BudgetReAppropriationMisc.workflow';
delete from EG_SCRIPT where name='BudgetReAppropriationMisc.workflow.validactions';