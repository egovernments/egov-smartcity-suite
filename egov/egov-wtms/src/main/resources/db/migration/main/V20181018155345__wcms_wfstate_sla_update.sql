
update eg_wf_states states
set sla=(states.createddate + interval '1 day' * apt.processingtime) 
from egwtr_connectiondetails connectiondetails 
inner join egwtr_application_type apptype on apptype.id = connectiondetails.applicationtype
inner join egwtr_application_process_time apt on apt.applicationtype=connectiondetails.applicationtype
inner join egwtr_category category on connectiondetails.category=category.id
where states.id = connectiondetails.state_id;


update eg_wf_state_history  statehistory
set sla=(statehistory.createddate + interval '1 day' * apt.processingtime) 
from egwtr_connectiondetails connectiondetails 
inner join egwtr_application_type apptype on apptype.id = connectiondetails.applicationtype
inner join egwtr_application_process_time apt on apt.applicationtype=connectiondetails.applicationtype
inner join egwtr_category category on connectiondetails.category=category.id
where statehistory.state_id = connectiondetails.state_id;


