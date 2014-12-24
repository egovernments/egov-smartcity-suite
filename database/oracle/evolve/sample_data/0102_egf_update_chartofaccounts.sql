#UP
update chartofaccounts set classification=1 where length(glcode)=3;
update chartofaccounts set classification=2 where length(glcode)=5;
update chartofaccounts set classification=4 where length(glcode)=7;
#DOWN