alter table egswtax_feesdetail_master add column isFixedRate boolean default false;
alter table egswtax_feesdetail_master add column  amount double precision default 0;

update egswtax_feesdetail_master set amount=500,isfixedrate=true where code='INSPECTIONCHARGE';