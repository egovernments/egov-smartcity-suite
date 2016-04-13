
insert into egwtr_document_names (id,applicationtype,documentname,description,required,active,createddate,lastmodifieddate,
createdby,lastmodifiedby,version)values(nextval('seq_egwtr_document_names'),(select id from egwtr_application_type where code='NEWCONNECTION'),
'DemandBill','DemandBill',false,true,now(),now(),1,1,0);


insert into egwtr_document_names (id,applicationtype,documentname,description,required,active,createddate,lastmodifieddate,
createdby,lastmodifiedby,version)values(nextval('seq_egwtr_document_names'),
(select id from egwtr_application_type where code='ADDNLCONNECTION'),
'DemandBill','DemandBill',false,true,now(),now(),1,1,0);



insert into egwtr_document_names (id,applicationtype,documentname,description,required,active,createddate,lastmodifieddate,
createdby,lastmodifiedby,version)values(nextval('seq_egwtr_document_names')
,(select id from egwtr_application_type where code='CHANGEOFUSE'),
'DemandBill','DemandBill',false,true,now(),now(),1,1,0);

insert into egwtr_document_names (id,applicationtype,documentname,description,required,active,createddate,lastmodifieddate,
createdby,lastmodifiedby,version)values(nextval('seq_egwtr_document_names')
,(select id from egwtr_application_type where code='RECONNECTION'),
'DemandBill','DemandBill',false,true,now(),now(),1,1,0);