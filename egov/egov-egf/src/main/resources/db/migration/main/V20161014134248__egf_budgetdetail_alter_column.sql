alter table egf_budgetdetail rename modifieddate to lastmodifieddate; 

alter table egf_budgetdetail rename modifiedby to lastmodifiedby; 

alter table egf_budgetdetail add column version numeric;

alter table eg_dept_functionmap add column budgetaccount_type  character varying(256);