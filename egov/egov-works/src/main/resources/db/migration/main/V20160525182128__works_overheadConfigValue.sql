------------------START---------------
Insert into eg_appconfig_values(id,key_id, effective_from,value,createddate,lastmodifieddate,createdby,lastmodifiedby,version)
values(nextval('SEQ_EG_APPCONFIG_VALUES'),(select id from  eg_appconfig where key_name='OVERHEAD_PURPOSE'),now(),(select id from egf_accountcode_purpose where name='Overhead_account codes'),now(),now(),1,1,0);
---------------END--------------------
