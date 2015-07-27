Insert into egf_accountcode_purpose (ID,NAME,MODIFIEDDATE,MODIFIEDBY,CREATEDDATE,CREATEDBY) values 
(121,'Cash In Transit',current_date,1,current_date,1);

update chartofaccounts set purposeid = 121 where glcode='4501002';
