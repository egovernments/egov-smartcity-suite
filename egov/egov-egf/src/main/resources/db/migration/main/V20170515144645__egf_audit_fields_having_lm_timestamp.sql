

alter table accountdetailkey  add column createddate timestamp default now();
alter table accountdetailkey  add column createdby integer default 1;
alter table accountdetailkey  add column lastmodifiedby integer default 1;
alter table accountdetailkey  add column version integer default 0; 



alter table functionary  add column lastmodifieddate timestamp default now();
alter table functionary  add column createddate timestamp default now();
alter table functionary  add column createdby integer default 1;
alter table functionary  add column lastmodifiedby integer default 1;

alter table accountdetailtype  add column createdby integer default 1; 
alter table accountdetailtype  alter column lastmodifieddate type timestamp without time zone;
alter table accountdetailtype  alter column lastmodifieddate set default now();


alter table  bank  alter column lastmodifieddate type timestamp without time zone;
alter table  bank  alter column lastmodifieddate set default now();

alter table  bankbranch  alter column lastmodifieddate type timestamp without time zone;
alter table  bankbranch  alter column lastmodifieddate set default now();

alter table  fund  alter column lastmodifieddate type timestamp without time zone;
alter table  fund  alter column lastmodifieddate set default now();

alter table  function  alter column lastmodifieddate type timestamp without time zone;
alter table  function  alter column lastmodifieddate set default now();

alter table  chartofaccounts  alter column lastmodifieddate type timestamp without time zone;
alter table  chartofaccounts  alter column lastmodifieddate set default now();

alter table  chartofaccountdetail  alter column lastmodifieddate type timestamp without time zone;
alter table  chartofaccountdetail  alter column lastmodifieddate set default now();


