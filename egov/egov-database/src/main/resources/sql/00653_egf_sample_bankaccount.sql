insert into bankbranch(id,branchcode,branchname,branchaddress1,bankid,isactive,created,lastmodified,modifiedby)
 values(nextval('seq_bankbranch'),'001','Main branch','Srikakulam',(select id from bank where name='Allahabad UP Gramin Bank'),1,current_date,current_date,1);


insert into chartofaccounts values(nextval('seq_chartofaccounts'),'4504204',
'Allahabad UP Gramin Bank-Main branch-000000000001','Allahabad UP Gramin Bank-Main branch-000000000001',
true,(select id from chartofaccounts where glcode='45042'),current_date,1,current_date,null,null,'A',null,4,true,false,null,null,null,null,null,450,1,null,null);


insert into bankaccount(id,branchid,accountnumber,accounttype,narration,isactive,glcodeid,fundid,type,createdby,lastmodifiedby,createddate,lastmodifieddate,version,currentbalance)
values(nextval('seq_bankaccount'),(select id from bankbranch where branchcode='001'),'000000000001','OTHER SCHEDULED BANKS',null,1,
(select id from chartofaccounts where glcode='4504204'),(select id from fund where code='01'),'RECEIPTS_PAYMENTS',1,1,current_date,current_date,0,0);


