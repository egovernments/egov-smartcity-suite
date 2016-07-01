------------------START---------------
delete from egf_accountcode_purpose where name = 'Retention Money';
---------------END--------------------


--rollback Insert into egf_accountcode_purpose (id,name,modifieddate,modifiedby,createddate,createdby) values (122,'Retention Money',now(),1,now(),1);
