#UP

update chartofaccounts set purposeid=(select id from EGF_ACCOUNTCODE_PURPOSE where name like 'RETENTION_MONEY') 
where glcode in ('3401002');


#DOWN

update chartofaccounts set purposeid=null where glcode in ('3401002');

