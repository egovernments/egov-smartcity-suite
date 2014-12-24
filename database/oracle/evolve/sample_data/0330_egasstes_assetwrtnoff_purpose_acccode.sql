#UP
update  chartofaccounts set purposeid =(select id from egf_accountcode_purpose where name='Fixed Assets Written off' )  
where glcode='2704003';
#DOWN
update  chartofaccounts set purposeid = null where glcode='2704003';