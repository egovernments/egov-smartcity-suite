Delete from egswtax_feesdetail_master where code in ('DONATIONCHARGE','DONATIONCHARGES');

insert into egswtax_feesdetail_master(id,description,code,fees,ismandatory,isactive,createddate,createdby,lastmodifieddate,lastmodifiedby,version) values (nextval('seq_egswtax_feesdetail_master'),'Donation Charge','DONATIONCHARGE',(select id from egswtax_fees_master where code='SEWERAGETAX'),'f','t',now(),1,now(),1,0);

update eg_demand_reason_master set code='DONATIONCHARGE' where code='DONATIONCHARGES' and module=(select id from eg_module where name='Sewerage Tax Management');
