------ Fees Master data ----------------------

insert into egswtax_fees_master(id,description,code,applicationtype,createddate,createdby,lastmodifieddate,lastmodifiedby,version) values (nextval('seq_egswtax_fees_master'),'Estimation Charge','ESTIMATIONCHARGE',(select id from egswtax_application_type where code='NEWSEWERAGECONNECTION' and active='t'),now(),1,now(),1,0);

------ Fees Detail Master data ----------------------

update egswtax_feesdetail_master set fees=(select id from egswtax_fees_master where code='ESTIMATIONCHARGE') where code='ESTIMATIONCHARGE';

insert into egswtax_feesdetail_master(id,description,code,fees,ismandatory,isactive,createddate,createdby,lastmodifieddate,lastmodifiedby,version) values (nextval('seq_egswtax_feesdetail_master'),'Sewerage Tax','SEWERAGETAX',(select id from egswtax_fees_master where code='SEWERAGETAX'),'f','t',now(),1,now(),1,0);

