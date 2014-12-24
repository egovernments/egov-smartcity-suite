
#UP


INSERT INTO EG_DEMAND_DETAILS ( ID, ID_DEMAND, ID_DEMAND_REASON, ID_STATUS, FILE_REFERENCE_NO,
REMARKS, AMOUNT, LAST_UPDATED_TIMESTAMP, CREATE_TIME_STAMP,AMT_COLLECTED ) VALUES ( 
SEQ_EG_DEMAND_DETAILS.nextval, (select min(id) from eg_demand),(select max(id) from eg_demand_reason where ID_DEMAND_REASON_MASTER=(select id from EG_DEMAND_REASON_MASTER where CODE='GENTAX-RESD')
 and ID_INSTALLMENT = (SELECT id_installment FROM eg_installment_master WHERE start_date <= trunc(SYSDATE) AND end_date >= trunc(SYSDATE) and id_module =(select id_module from eg_module where module_name='Property Tax'))
 and PURPOSEID = (select id from EGF_ACCOUNTCODE_PURPOSE where name = 'GENERALTAX-RESIDENTIAL'))
, NULL, NULL, NULL, 40620,  sysdate,  sysdate,200); 

INSERT INTO EG_DEMAND_DETAILS ( ID, ID_DEMAND, ID_DEMAND_REASON, ID_STATUS, FILE_REFERENCE_NO,
REMARKS, AMOUNT, LAST_UPDATED_TIMESTAMP, CREATE_TIME_STAMP,AMT_COLLECTED ) VALUES ( 
SEQ_EG_DEMAND_DETAILS.nextval, (select min(id) from eg_demand),(select max(id)  from eg_demand_reason where ID_DEMAND_REASON_MASTER=(select id from EG_DEMAND_REASON_MASTER where CODE='EDUTAX-RESD')
 and ID_INSTALLMENT = (SELECT id_installment FROM eg_installment_master WHERE start_date <= trunc(SYSDATE) AND end_date >= trunc(SYSDATE) and id_module =(select id_module from eg_module where module_name='Property Tax'))
 and PURPOSEID = (select id from EGF_ACCOUNTCODE_PURPOSE where name = 'ELEMENTARYEDUCATIONTAX-RESIDENTIAL'))
, NULL, NULL, NULL, 40620,  sysdate,  sysdate,100); 


INSERT INTO EG_DEMAND_DETAILS ( ID, ID_DEMAND, ID_DEMAND_REASON, ID_STATUS, FILE_REFERENCE_NO,
REMARKS, AMOUNT, LAST_UPDATED_TIMESTAMP, CREATE_TIME_STAMP,AMT_COLLECTED ) VALUES ( 
SEQ_EG_DEMAND_DETAILS.nextval, (select min(id) from eg_demand),(select max(id)  from eg_demand_reason where ID_DEMAND_REASON_MASTER=(select id from EG_DEMAND_REASON_MASTER where CODE='LIGHTAX-RESD')
 and ID_INSTALLMENT = (SELECT id_installment FROM eg_installment_master WHERE start_date <= trunc(SYSDATE) AND end_date >= trunc(SYSDATE) and id_module =(select id_module from eg_module where module_name='Property Tax'))
 and PURPOSEID = (select id from EGF_ACCOUNTCODE_PURPOSE where name = 'LIGHTINGTAX-RESIDENTIAL'))
, NULL, NULL, NULL, 40620,  sysdate,  sysdate,50); 


INSERT INTO EG_DEMAND_DETAILS ( ID, ID_DEMAND, ID_DEMAND_REASON, ID_STATUS, FILE_REFERENCE_NO,
REMARKS, AMOUNT, LAST_UPDATED_TIMESTAMP, CREATE_TIME_STAMP,AMT_COLLECTED ) VALUES ( 
SEQ_EG_DEMAND_DETAILS.nextval, (select min(id) from eg_demand),(select max(id)  from eg_demand_reason where ID_DEMAND_REASON_MASTER=(select id from EG_DEMAND_REASON_MASTER where CODE='LIBCESS-RESD')
 and ID_INSTALLMENT = (SELECT id_installment FROM eg_installment_master WHERE start_date <= trunc(SYSDATE) AND end_date >= trunc(SYSDATE) and id_module =(select id_module from eg_module where module_name='Property Tax'))
 and PURPOSEID = (select id from EGF_ACCOUNTCODE_PURPOSE where name = 'LIABRARYCESS-RESIDENTIAL'))
, NULL, NULL, NULL, 40620,  sysdate,  sysdate,50); 



INSERT INTO EG_DEMAND_DETAILS ( ID, ID_DEMAND, ID_DEMAND_REASON, ID_STATUS, FILE_REFERENCE_NO,
REMARKS, AMOUNT, LAST_UPDATED_TIMESTAMP, CREATE_TIME_STAMP,AMT_COLLECTED ) VALUES ( 
SEQ_EG_DEMAND_DETAILS.nextval, (select min(id) from eg_demand),(select max(id) from eg_demand_reason where ID_DEMAND_REASON_MASTER=(select id from EG_DEMAND_REASON_MASTER where CODE='GENTAX-RESD')
 and ID_INSTALLMENT = (SELECT id_installment FROM eg_installment_master WHERE start_date like to_date('04/01/1993','MM/DD/YYYY') and id_module =(select id_module from eg_module where module_name='Property Tax'))
 and PURPOSEID = (select id from EGF_ACCOUNTCODE_PURPOSE where name = 'GENERALTAX-RESIDENTIAL'))
, NULL, NULL, NULL, 40620,  sysdate,  sysdate,300); 

INSERT INTO EG_DEMAND_DETAILS ( ID, ID_DEMAND, ID_DEMAND_REASON, ID_STATUS, FILE_REFERENCE_NO,
REMARKS, AMOUNT, LAST_UPDATED_TIMESTAMP, CREATE_TIME_STAMP,AMT_COLLECTED ) VALUES ( 
SEQ_EG_DEMAND_DETAILS.nextval, (select min(id) from eg_demand),(select max(id)  from eg_demand_reason where ID_DEMAND_REASON_MASTER=(select id from EG_DEMAND_REASON_MASTER where CODE='EDUTAX-RESD')
 and ID_INSTALLMENT = (SELECT id_installment FROM eg_installment_master WHERE start_date like to_date('04/01/1993','MM/DD/YYYY') and id_module =(select id_module from eg_module where module_name='Property Tax'))
 and PURPOSEID = (select id from EGF_ACCOUNTCODE_PURPOSE where name = 'ELEMENTARYEDUCATIONTAX-RESIDENTIAL'))
, NULL, NULL, NULL, 40620,  sysdate,  sysdate,200); 


INSERT INTO EG_DEMAND_DETAILS ( ID, ID_DEMAND, ID_DEMAND_REASON, ID_STATUS, FILE_REFERENCE_NO,
REMARKS, AMOUNT, LAST_UPDATED_TIMESTAMP, CREATE_TIME_STAMP,AMT_COLLECTED ) VALUES ( 
SEQ_EG_DEMAND_DETAILS.nextval, (select min(id) from eg_demand),(select max(id)  from eg_demand_reason where ID_DEMAND_REASON_MASTER=(select id from EG_DEMAND_REASON_MASTER where CODE='LIGHTAX-RESD')
 and ID_INSTALLMENT = (SELECT id_installment FROM eg_installment_master WHERE start_date like to_date('04/01/1993','MM/DD/YYYY') and id_module =(select id_module from eg_module where module_name='Property Tax'))
 and PURPOSEID = (select id from EGF_ACCOUNTCODE_PURPOSE where name = 'LIGHTINGTAX-RESIDENTIAL'))
, NULL, NULL, NULL, 40620,  sysdate,  sysdate,200); 


INSERT INTO EG_DEMAND_DETAILS ( ID, ID_DEMAND, ID_DEMAND_REASON, ID_STATUS, FILE_REFERENCE_NO,
REMARKS, AMOUNT, LAST_UPDATED_TIMESTAMP, CREATE_TIME_STAMP,AMT_COLLECTED ) VALUES ( 
SEQ_EG_DEMAND_DETAILS.nextval, (select min(id) from eg_demand),(select max(id)  from eg_demand_reason where ID_DEMAND_REASON_MASTER=(select id from EG_DEMAND_REASON_MASTER where CODE='LIBCESS-RESD')
 and ID_INSTALLMENT = (SELECT id_installment FROM eg_installment_master WHERE start_date like to_date('04/01/1993','MM/DD/YYYY') and id_module =(select id_module from eg_module where module_name='Property Tax'))
 and PURPOSEID = (select id from EGF_ACCOUNTCODE_PURPOSE where name = 'LIABRARYCESS-RESIDENTIAL'))
, NULL, NULL, NULL, 40620,  sysdate,  sysdate,100); 




update eg_demand set AMT_COLLECTED=1200;

commit;

#DOWN





