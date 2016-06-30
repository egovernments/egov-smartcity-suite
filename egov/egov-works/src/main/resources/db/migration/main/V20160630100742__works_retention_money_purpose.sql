------------------START---------------
Insert into egf_accountcode_purpose (id,name,modifieddate,modifiedby,createddate,createdby) values (122,'Retention Money',now(),1,now(),1);
---------------END--------------------


--rollback delete from egf_accountcode_purpose where name = 'Retention Money';
