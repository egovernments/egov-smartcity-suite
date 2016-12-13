------ START : Insert New status for workflow ---
Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (nextval('SEQ_EGW_STATUS'),'ADVANCEBILL','Created',now(),'CREATED',0);
Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (nextval('SEQ_EGW_STATUS'),'ADVANCEBILL','Approved',now(),'APPROVED',1);
Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (nextval('SEQ_EGW_STATUS'),'ADVANCEBILL','Cancelled',now(),'CANCELLED',2);
------ END : Insert New status for workflow ---

--rollback delete from EGW_STATUS where MODULETYPE = 'ADVANCEBILL';