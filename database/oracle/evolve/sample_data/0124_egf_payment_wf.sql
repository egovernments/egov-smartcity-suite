#UP
update EG_SCRIPT set SCRIPT='transitions={''true'':[''true''],''false'':[''false'']}
state=''false''   
val=persistenceService.getFunctionaryAndDesignation()
departmentdefaultAndDisabled=''false''
if((val==''FMU-ASSISTANT'') and purpose==''balancecheck''):  
    state=''true'' 
if(val==''UAC-ASSISTANT'' and purpose==''createpayment''):  
    state=''true''
if(purpose==''deptcheck''):
    state= departmentdefaultAndDisabled  
if state in transitions:result=transitions[state]  
' where name='Paymentheader.show.bankbalance' ; 

#DOWN