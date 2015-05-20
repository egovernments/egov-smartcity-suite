Insert into egf_accountcode_purpose (id,name,modifieddate,modifiedby,createddate,createdby) values (nextval('seq_egf_accountcode_purpose'),'Fixed Assets',null,null,null,null);
Insert into egf_accountcode_purpose (id,name,modifieddate,modifiedby,createddate,createdby) values (nextval('seq_egf_accountcode_purpose'),'Accumulated Depreciation',null,null,null,null);
Insert into egf_accountcode_purpose (id,name,modifieddate,modifiedby,createddate,createdby) values (nextval('seq_egf_accountcode_purpose'),'Depreciation Expense Account',null,null,null,null);
Insert into egf_accountcode_purpose (id,name,modifieddate,modifiedby,createddate,createdby) values (nextval('seq_egf_accountcode_purpose'),'Revaluation Reserve Account',null,null,null,null);

--rollback delete from egf_accountcode_purpose where name in('Fixed Assets','Accumulated Depreciation','Depreciation Expense Account','Revaluation Reserve Account');
