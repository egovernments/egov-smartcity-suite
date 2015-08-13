update chartofaccounts set purposeid =
(select id from EGF_ACCOUNTCODE_PURPOSE where name='Cash In Hand') where glcode='4501001' and purposeid is null;

update chartofaccounts set purposeid =
(select id from EGF_ACCOUNTCODE_PURPOSE where name='Cheque In Hand') where glcode='4501051' and purposeid is null;
