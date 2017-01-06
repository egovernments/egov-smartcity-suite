INSERT INTO egcl_servicecategory(id, name, code, isactive, version, createdby, createddate, lastmodifiedby, lastmodifieddate)
VALUES(nextval('seq_egcl_servicecategory'), 'SBIMOPS Payment Gateway', 'SBIMOPS', false, 0, 1, current_timestamp, 1, current_timestamp);

Insert into egcl_servicedetails (ID,NAME,SERVICEURL,ISENABLED,CALLBACKURL,SERVICETYPE,CODE,FUND,FUNDSOURCE,FUNCTIONARY,
VOUCHERCREATION,SCHEME,SUBSCHEME,SERVICECATEGORY,ISVOUCHERAPPROVED,VOUCHERCUTOFFDATE,CREATED_BY,created_date,
modified_by,modified_date) values 
(nextval('seq_egcl_servicedetails'),'SBI Bank Payment Gateway','https://treasury.ap.gov.in/cybertry/deptrequest_mops.php',true,null,
'P','SBIMOPS',null,null,null,true,null,null,(select id from egcl_servicecategory where code='SBIMOPS'),true,null,1,current_timestamp,1,current_timestamp);
