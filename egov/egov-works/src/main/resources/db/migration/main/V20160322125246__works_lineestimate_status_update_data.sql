------ START : Line Estimate application status ---
Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (nextval('SEQ_EGW_STATUS'),'LINEESTIMATE','Rejected',now(),'REJECTED',6);
------ END : Line Estimate application status ---

--rollback delete from egw_status where moduletype = 'LINEESTIMATE' and code = 'REJECTED';