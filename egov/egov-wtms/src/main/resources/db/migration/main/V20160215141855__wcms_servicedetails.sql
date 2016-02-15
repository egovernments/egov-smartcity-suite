Insert into egcl_servicedetails (ID,NAME,SERVICEURL,ISENABLED,CALLBACKURL,SERVICETYPE,CODE,FUND,FUNDSOURCE,FUNCTIONARY,
VOUCHERCREATION,SCHEME,SUBSCHEME,SERVICECATEGORY,ISVOUCHERAPPROVED,VOUCHERCUTOFFDATE,CREATED_BY,created_date,
modified_by,modified_date) values 
(nextval('seq_egcl_servicedetails'),'Water Estimation Charges','/../wtms/application/generatebill',true,
'/receipts/receipt-create.action', 'B','WES',
(select id from fund where code='01'),null,null,true,null,null,(select id from egcl_servicecategory where code='WT'),true,
null,1,now(),1,now());