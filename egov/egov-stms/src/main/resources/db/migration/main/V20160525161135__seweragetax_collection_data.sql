-- Sequence to generate bill number for sewerage fee collection
create sequence SEQ_SEWERAGEBILL_NUMBER;

-- Service Detail for sewerage tax
Insert into egcl_servicedetails (ID,NAME,SERVICEURL,ISENABLED,CALLBACKURL,SERVICETYPE,CODE,FUND,FUNDSOURCE,FUNCTIONARY,VOUCHERCREATION,SCHEME,SUBSCHEME,SERVICECATEGORY,ISVOUCHERAPPROVED,VOUCHERCUTOFFDATE,CREATED_BY,created_date,modified_by,modified_date) values (nextval('seq_egcl_servicedetails'),'Sewerage Tax','/../stms/collection/generatebill',true,'/receipts/receipt-create.action','B','STAX',null,null,null,true,null,null,(select id from egcl_servicecategory where code='STAX'),true,null,1,current_timestamp,1,current_timestamp);

