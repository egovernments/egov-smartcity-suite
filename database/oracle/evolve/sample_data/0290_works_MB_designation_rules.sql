#UP
UPDATE EG_SCRIPT set SCRIPT=
'result=['''','''']
print wfItem
if wfItem:    
    if wfItem.getCurrentState():
	currentDesig=wfItem.getCurrentState().getOwner().getDesigId().getDesignationName()
	action=wfItem.getCurrentState().getNextAction()
	if action:
    	    state=wfItem.getCurrentState().getValue()+''-''+action
    	else: 
	    state=wfItem.getCurrentState().getValue()
	print state
if ((state == ''DEFAULT'' or state == ''NEW'' or state == ''REJECTED'')  and department == ''L-Electrical''):
    result[0]="ASSISTANT DIVISIONAL ENGINEER"
elif ((state == ''DEFAULT'' or state == ''NEW'' or state == ''REJECTED'') and department == ''''):
    result[0]="ASSISTANT EXECUTIVE ENGINEER" 
    result[1]="ASSISTANT DIVISIONAL ENGINEER"
elif ((state == ''DEFAULT'' or state == ''NEW'' or state == ''REJECTED'') and department != ''L-Electrical''):
    result[0]="ASSISTANT EXECUTIVE ENGINEER" 
elif (state == ''CREATED-Pending for check'' or state == ''RESUBMITTED-Pending for check'') and currentDesig == ''ASSISTANT DIVISIONAL ENGINEER'':
    result[0]="DIVISIONAL ELECTRICAL ENGINEER" 
elif (state == ''CREATED-Pending for check'' or state == ''RESUBMITTED-Pending for check'') and currentDesig == ''ASSISTANT EXECUTIVE ENGINEER'':
    result[0]="EXECUTIVE ENGINEER" 
elif ((state == ''CREATED-Pending for check'' or state == ''RESUBMITTED-Pending for check'') and (currentDesig == ''EXECUTIVE ENGINEER'' or currentDesig == ''DIVISIONAL ELECTRICAL ENGINEER'')):
    result[0]="ASSISTANT"
elif (state == ''CHECKED-Pending checking for correctness''):
    result[0]="SECTION MANAGER"
'
WHERE NAME='MBHeader.nextDesignation';

#DOWN

UPDATE EG_SCRIPT set SCRIPT=
'result=['''','''']
print wfItem 
workValue=0
if wfItem:    
    if wfItem.getCurrentState():
	currentDesig=wfItem.getCurrentState().getOwner().getDesigId().getDesignationName()
	action=wfItem.getCurrentState().getNextAction()
	if action:
    	    state=wfItem.getCurrentState().getValue()+''-''+action
    	else: 
	    state=wfItem.getCurrentState().getValue()
	print state
print workValue
if ((state == ''DEFAULT'' or state == ''NEW'' or state == ''REJECTED'')  and department == ''L-Electrical''):
    result[0]="ASSISTANT DIVISIONAL ENGINEER"
elif ((state == ''DEFAULT'' or state == ''NEW'' or state == ''REJECTED'') and department == ''''):
    result[0]="ASSISTANT EXECUTIVE ENGINEER" 
    result[1]="ASSISTANT DIVISIONAL ENGINEER"
elif ((state == ''DEFAULT'' or state == ''NEW'' or state == ''REJECTED'') and department != ''L-Electrical''):
    result[0]="ASSISTANT EXECUTIVE ENGINEER" 
elif (state == ''CREATED-Pending for check'' or state == ''RESUBMITTED-Pending for check''):
    result[0]="SUPERINTENDING ENGINEER" 
elif (state == ''CHECKED-Pending for Approval'' or state == ''RESUBMITTED-Pending for Approval''):
    result[0]="SUPERINTENDING ENGINEER"
'
WHERE NAME='MBHeader.nextDesignation';
