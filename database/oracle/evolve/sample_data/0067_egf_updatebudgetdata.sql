#UP

update eg_wf_states set modified_by=1,created_by=1,created_date=sysdate,modified_date=sysdate where type like 'Budget%';

#DOWN