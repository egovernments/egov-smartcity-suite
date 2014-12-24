#UP

update chartofaccounts set majorcode = substr(glcode,1,3);
commit;

#DOWN