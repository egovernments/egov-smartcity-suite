#UP
update   chartofaccounts set budgetcheckreq=1  where glcode='1401102';
#DOWN
update   chartofaccounts set budgetcheckreq=0  where glcode='1401102';