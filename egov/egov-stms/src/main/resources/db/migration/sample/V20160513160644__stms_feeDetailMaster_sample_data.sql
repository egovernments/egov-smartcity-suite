------ Fees Detail Master sample data ----------------------

insert into egswtax_feesdetail_master(id,description,code,fees,ismandatory,isactive,createddate,createdby,lastmodifieddate,lastmodifiedby,version) values (nextval('seq_egswtax_feesdetail_master'),'Donation Charges','DONATIONCHARGES',(select id from egswtax_fees_master where code='SEWERAGETAX'),'f','t',now(),1,now(),1,0);

