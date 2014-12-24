#UP

INSERT INTO EG_DEMAND_DETAILS 
	(ID, ID_DEMAND, ID_DEMAND_REASON, ID_STATUS, FILE_REFERENCE_NO, REMARKS, AMOUNT, LAST_UPDATED_TIMESTAMP, CREATE_TIME_STAMP,AMT_COLLECTED ) 
	VALUES ( 
SEQ_EG_DEMAND_DETAILS.nextval, (select min(id) from eg_demand),(select max(id) from eg_demand_reason where ID_DEMAND_REASON_MASTER=(select id from EG_DEMAND_REASON_MASTER where CODE='PENALTY')  and ID_INSTALLMENT = (SELECT id_installment FROM eg_installment_master WHERE start_date <= trunc(SYSDATE) AND end_date >= trunc(SYSDATE) and id_module =(select id_module from eg_module where module_name='Property Tax')) and PURPOSEID = (select id from EGF_ACCOUNTCODE_PURPOSE where upper(name) = 'PTPENALTYCODE')), NULL, NULL, NULL, 400,  sysdate,  sysdate,200);

#DOWN

delete EG_DEMAND_DETAILS where id_demand_reason=(select max(id) from eg_demand_reason where ID_DEMAND_REASON_MASTER=(select id from EG_DEMAND_REASON_MASTER where CODE='PENALTY')
 and ID_INSTALLMENT = (SELECT id_installment FROM eg_installment_master WHERE start_date <= trunc(SYSDATE) AND end_date >= trunc(SYSDATE) and id_module =(select id_module from eg_module where module_name='Property Tax'))
 and PURPOSEID = (select id from EGF_ACCOUNTCODE_PURPOSE where upper(name) = 'PTPENALTYCODE'));
