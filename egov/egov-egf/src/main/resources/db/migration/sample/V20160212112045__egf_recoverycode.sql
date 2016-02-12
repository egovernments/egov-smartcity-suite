
insert into chartofaccountdetail(id,glcodeid,detailtypeid,createdby,createddate) values(nextval('seq_chartofaccountdetail'),
(select id from chartofaccounts where glcode='3502054'),(select id from accountdetailtype where name='Supplier'),1,current_date);

insert into chartofaccountdetail(id,glcodeid,detailtypeid,createdby,createddate) values(nextval('seq_chartofaccountdetail'),
(select id from chartofaccounts where glcode='3502054'),(select id from accountdetailtype where name='contractor'),1,current_date);

insert into tds (id,type,glcodeid,isactive,createdby,remitted,recoveryname,recovery_mode,remittance_mode) values 
(nextval('seq_tds'),'Service Tax',(select id from chartofaccounts where glcode='3502054'),1,1,'Chairman','3502054','M','M');

insert into chartofaccountdetail(id,glcodeid,detailtypeid,createdby,createddate) values(nextval('seq_chartofaccountdetail'),
(select id from chartofaccounts where glcode='3502001'),(select id from accountdetailtype where name='Employee'),1,current_date);

insert into tds (id,type,glcodeid,isactive,createdby,remitted,recoveryname,recovery_mode,remittance_mode) values 
(nextval('seq_tds'),'Employee GPF',(select id from chartofaccounts where glcode='3502001'),1,1,'PF officer','3502001','M','M');