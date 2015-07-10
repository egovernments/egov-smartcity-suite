INSERT INTO EG_DEMAND_REASON_MASTER(ID, REASONMASTER, "category", ISDEBIT, MODULE, CODE, "order", CREATE_DATE, MODIFIED_DATE) 
VALUES(nextval('SEQ_EG_DEMAND_REASON_MASTER'), 'PENALTY_FINES',(select ID from EG_REASON_CATEGORY where NAME='FINES'), 'N', (select id from eg_module where NAME='Property Tax'), 'PENALTY_FINES', 5, now(),  now());

INSERT INTO EG_DEMAND_REASON (ID, ID_DEMAND_REASON_MASTER, ID_INSTALLMENT, PERCENTAGE_BASIS, ID_BASE_REASON, CREATE_DATE, MODIFIED_DATE, GLCODEID) 
SELECT nextval('SEQ_EG_DEMAND_REASON'), (select id from EG_DEMAND_REASON_MASTER where code = 'PENALTY_FINES'), id, null,null, now(), now(), (select id from chartofaccounts where glcode='1402002')
from eg_installment_master where id_module = (select id from eg_module where name='Property Tax') and end_date < now();
