Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (NEXTVAL('SEQ_EGW_STATUS'),'WorkOrder','Acceptance letter issued',now(),'ACCEPTANCE_LETTER_ISSUED',6);
Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (NEXTVAL('SEQ_EGW_STATUS'),'WorkOrder','Acceptance letter acknowledged',now(),'ACCEPTANCE_LETTER_ACKNOWLEDGED',7);
Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (NEXTVAL('SEQ_EGW_STATUS'),'WorkOrder','Agreement order signed',now(),'AGREEMENT_ORDER_SIGNED',8);
Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (NEXTVAL('SEQ_EGW_STATUS'),'WorkOrder','Work order acknowledged',now(),'WORK_ORDER_ACKNOWLEDGED',9);
Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (NEXTVAL('SEQ_EGW_STATUS'),'WorkOrder','Site handed over',now(),'SITE_HANDED_OVER',10);
Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (NEXTVAL('SEQ_EGW_STATUS'),'WorkOrder','Work commenced',now(),'WORK_COMMENCED',11);

--rollback delete from EGW_STATUS where MODULETYPE='WorkOrder' and code in ('Acceptance_letter_issued','Acceptance_letter_acknowledged','Agreement_order_signed','Work_order_acknowledged','Site_handed_over','Work_commenced');
