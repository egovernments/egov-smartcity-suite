---eg_action---
update eg_action set url='/vacancyremissionapproval/create' where url='/vacancyremission/approval/' ;
update eg_action set url='/vacancyremissionapproval/update' where url='/vacancyremission/approval/update/' ;

---Workflow types---
update eg_wf_types set link='/ptis/vacancyremissionapproval/update/:ID' where type='VacancyRemissionApproval';