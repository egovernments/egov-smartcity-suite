ALTER TABLE egf_budgetdetail ADD COLUMN status bigint;

Insert into egw_status (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (nextval('seq_egw_status'),'BUDGETDETAIL','Created',current_date,'Created',null);

Insert into egw_status (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (nextval('seq_egw_status'),'BUDGETDETAIL','Approved',current_date,'Approved',null);

