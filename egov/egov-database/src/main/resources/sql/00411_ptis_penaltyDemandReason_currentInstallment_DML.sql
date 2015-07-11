INSERT INTO EG_DEMAND_REASON (ID, ID_DEMAND_REASON_MASTER, ID_INSTALLMENT, PERCENTAGE_BASIS, ID_BASE_REASON, CREATE_DATE, MODIFIED_DATE, GLCODEID) 
SELECT nextval('SEQ_EG_DEMAND_REASON'), (select id from EG_DEMAND_REASON_MASTER where code = 'PENALTY_FINES'), id, null,null, now(), now(), (select id from chartofaccounts where glcode='1402002')
FROM eg_installment_master where id_module = (select id from eg_module where name='Property Tax') and now() between start_date and end_date;
