

alter table egswtax_applicationdetails  rename column approvalnumber to estimationnumber;
alter table egswtax_applicationdetails  rename column approvaldate to estimationdate;

--rollback alter table egswtax_applicationdetails rename column estimationnumber to approvalnumber;
--rollback alter table egswtax_applicationdetails rename estimationdate to approvaldate;

