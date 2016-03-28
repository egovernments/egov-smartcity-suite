ALTER TABLE egf_reappropriation_misc ADD COLUMN status bigint;

Insert into egw_status (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (nextval('seq_egw_status'),'REAPPROPRIATIONMISC','Created',current_date,'Created',null);

Insert into egw_status (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (nextval('seq_egw_status'),'REAPPROPRIATIONMISC','Approved',current_date,'Approved',null);

update egw_status set code = 'New' where moduletype = 'BudgetReAppropriation' and description = 'New';

update egw_status set code = 'Approved' where moduletype = 'BudgetReAppropriation' and description = 'Approved';

update egw_status set code = 'Cancelled' where moduletype = 'BudgetReAppropriation' and description = 'Cancelled';
