
alter table eg_wf_matrix add column fromdate date;

alter table eg_wf_matrix add column todate date;

--rollback alter table eg_wf_matrix drop column fromdate;

--rollback alter table eg_wf_matrix drop column todate;
