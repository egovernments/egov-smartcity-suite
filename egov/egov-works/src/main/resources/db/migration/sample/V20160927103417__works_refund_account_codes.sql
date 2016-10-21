update chartofaccounts set purposeid=(select id from egf_accountcode_purpose where name='RETENTION_MONEY') where glcode='3401002' and purposeId is null;
update chartofaccounts set purposeid=(select id from egf_accountcode_purpose where name='Security Deposit') where glcode='3401003' and purposeId is null;


