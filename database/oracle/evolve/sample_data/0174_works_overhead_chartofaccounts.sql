#UP
update chartofaccounts set purposeid=60 where glcode in ('2308008','2308013','2308007','2308005');

#DOWN
update chartofaccounts set purposeid=null where glcode in ('2308008','2308013','2308007','2308005');
