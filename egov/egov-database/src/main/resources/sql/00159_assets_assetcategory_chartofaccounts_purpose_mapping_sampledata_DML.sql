update chartofaccounts set purposeid =
(select id from EGF_ACCOUNTCODE_PURPOSE where name='Fixed Assets') where glcode='410' and purposeid is null;
update chartofaccounts set purposeid =
(select id from EGF_ACCOUNTCODE_PURPOSE where name='Accumulated Depreciation') where glcode='411' and purposeid is null;
update chartofaccounts set purposeid =
(select id from EGF_ACCOUNTCODE_PURPOSE where name='Depreciation Expense Account') where glcode='272' and purposeid is null;
update chartofaccounts set purposeid =
(select id from EGF_ACCOUNTCODE_PURPOSE where name='Revaluation Reserve Account') where glcode='312' and purposeid is null;

--rollback update chartofaccounts set purposeid = null where glcode='410' and purposeid in(select id from EGF_ACCOUNTCODE_PURPOSE where name='Fixed Assets');
--rollback update chartofaccounts set purposeid = null where glcode='411' and purposeid in (select id from EGF_ACCOUNTCODE_PURPOSE where name='Accumulated Depreciation');
--rollback update chartofaccounts set purposeid = null where glcode='272' and purposeid in (select id from EGF_ACCOUNTCODE_PURPOSE where name='Depreciation Expense Account');
--rollback update chartofaccounts set purposeid = null where glcode='312' and purposeid in (select id from EGF_ACCOUNTCODE_PURPOSE where name='Revaluation Reserve Account');
