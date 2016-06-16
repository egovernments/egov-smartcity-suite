Insert into egcl_servicedetails (ID,NAME,SERVICEURL,ISENABLED,CALLBACKURL,SERVICETYPE,CODE,FUND,FUNDSOURCE,FUNCTIONARY,
VOUCHERCREATION,SCHEME,SUBSCHEME,SERVICECATEGORY,ISVOUCHERAPPROVED,VOUCHERCUTOFFDATE,CREATED_BY,created_date,
modified_by,modified_date) values 
(nextval('seq_egcl_servicedetails'),'Property Tax (On Land)','/../ptis/view/viewDCBProperty!displayPropInfo.action?propertyId=',true,'/receipts/receipt-create.action',
'B','VLT',1,null,null,false,null,null,(select id from egcl_servicecategory where code='PT'),false,null,1,current_timestamp,1,current_timestamp);

