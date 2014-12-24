#UP
update   chartofaccounts set budgetcheckreq=0  where glcode='2407001' and purposeid=30;
#DOWN
update   chartofaccounts set budgetcheckreq=1  where glcode='2407001' and purposeid=30;