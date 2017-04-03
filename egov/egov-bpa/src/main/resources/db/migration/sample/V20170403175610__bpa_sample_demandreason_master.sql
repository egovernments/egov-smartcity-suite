
Insert into eg_installment_master (ID,INSTALLMENT_NUM,INSTALLMENT_YEAR,START_DATE,END_DATE,ID_MODULE,
LASTUPDATEDTIMESTAMP,DESCRIPTION,INSTALLMENT_TYPE) values (nextval('SEQ_EG_INSTALLMENT_MASTER'),
042015,to_date('01-04-17','DD-MM-YY'),to_date('01-10-17','DD-MM-YY'),
to_date('31-03-16','DD-MM-YY'),(select id from eg_module where name = 'BPA'
 and parentmodule is null),current_timestamp,'BPATAX/1718','Yearly');


 INSERT INTO EG_DEMAND_REASON_MASTER ( ID, REASONMASTER, "category", ISDEBIT, module, CODE, 
 "order", create_date, modified_date,isdemand) VALUES(nextval('seq_eg_demand_reason_master'), 
 'ADMISSION FEES', (select id from eg_reason_category where code='FINES'), 'N',
  (select id from eg_module where name='BPA'), 'ADMISSIONFEES', 1, current_timestamp, current_timestamp,'t');


Insert into EG_DEMAND_REASON (ID,ID_DEMAND_REASON_MASTER,ID_INSTALLMENT,PERCENTAGE_BASIS,ID_BASE_REASON
,create_date,modified_date,GLCODEID) 
(select (nextval('seq_eg_demand_reason')), 
(select id from eg_demand_reason_master where reasonmaster='ADMISSION FEES' 
and module=(select id from eg_module where name='BPA')), inst.id, null, 
null, current_timestamp, current_timestamp, null from eg_installment_master inst where
 inst.id_module=(select id from eg_module where name='BPA'));

	



--colection


Insert into egcl_servicecategory (id,name,code,isactive,version,createdby,createddate,lastmodifiedby,
lastmodifieddate) values (nextval('SEQ_EG_MODULE'),'Bpa Fees',
'BPA',true,0,1,to_timestamp('2015-08-15 11:04:22.628834','null'),1,to_timestamp('2015-08-15 11:04:22.628834','null'));

Insert into egcl_servicedetails (ID,NAME,SERVICEURL,ISENABLED,CALLBACKURL,SERVICETYPE,CODE,FUND,FUNDSOURCE,FUNCTIONARY,
VOUCHERCREATION,SCHEME,SUBSCHEME,SERVICECATEGORY,ISVOUCHERAPPROVED,VOUCHERCUTOFFDATE,CREATED_BY,created_date,
modified_by,modified_date) values 
(nextval('seq_egcl_servicedetails'),'BPA AdmissionFee','/../bpa/application//newApplication-create',true,
'/receipts/receipt-create.action', 'B','BPADM',
(select id from fund where code='01'),null,null,true,null,null,(select id from egcl_servicecategory where code='BPA'),true,
null,1,now(),1,now());

