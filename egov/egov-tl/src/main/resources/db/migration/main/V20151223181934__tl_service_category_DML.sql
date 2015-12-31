
delete from egcl_servicedetails  where name='Trade License';
delete from egcl_servicecategory  where name='Trade License';

INSERT INTO egcl_servicecategory(id, name, code, isactive, version, createdby, createddate, lastmodifiedby, lastmodifieddate)
VALUES(nextval('seq_egcl_servicecategory'), 'Trade License', 'TL', true, 0, 1, current_timestamp, 1, current_timestamp);

Insert into egcl_servicedetails (ID,NAME,SERVICEURL,ISENABLED,CALLBACKURL,SERVICETYPE,CODE,FUND,FUNDSOURCE,FUNCTIONARY,
VOUCHERCREATION,SCHEME,SUBSCHEME,SERVICECATEGORY,ISVOUCHERAPPROVED,VOUCHERCUTOFFDATE,CREATED_BY,created_date,
modified_by,modified_date) values 
(nextval('seq_egcl_servicedetails'),'Trade License','/receipts/receipt-create.action',true,'/../tl/search/searchTrade-newForm.action', 'B','TL',(select id from fund where code='01'),null,null,true,null,null,(select id from egcl_servicecategory where code='TL'),true,to_date('11-07-12','DD-MM-RR'),1,current_timestamp,1,current_timestamp);

