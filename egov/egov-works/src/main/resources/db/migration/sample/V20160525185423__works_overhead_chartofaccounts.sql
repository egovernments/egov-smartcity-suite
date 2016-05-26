------------START------------------
update chartofaccounts set purposeid=(select id from egf_accountcode_purpose where name='Overhead_account codes') where glcode in ('2308016');
-----------END------------------------


--------rollback update chartofaccounts set purposeid=null where glcode in ('2308016');
