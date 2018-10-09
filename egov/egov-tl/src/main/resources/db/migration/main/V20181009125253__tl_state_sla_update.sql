update eg_wf_states set sla = (createddate + interval '1 day' * (select value from egtl_configuration where key = 'NEW_APPTYPE_DEFAULT_SLA') :: Integer) where id in (select state_id from egtl_license where licenseapptype = (select id from egtl_mstr_app_type where name = 'New License'));

update eg_wf_states set sla = (createddate + interval '1 day' * (select value from egtl_configuration where key = 'RENEW_APPTYPE_DEFAULT_SLA') :: Integer) where id in (select state_id from egtl_license where licenseapptype = (select id from egtl_mstr_app_type where name = 'License Renewal'));

update eg_wf_states set sla = (createddate + interval '1 day' * (select value from egtl_configuration where key = 'CLOSURE_APPTYPE_DEFAULT_SLA') :: Integer) where id in (select state_id from egtl_license where licenseapptype = (select id from egtl_mstr_app_type where name = 'License Closure'));

update eg_wf_state_history set sla = (createddate + interval '1 day' * (select value from egtl_configuration where key = 'NEW_APPTYPE_DEFAULT_SLA') :: Integer) where state_id in (select state_id from egtl_license where licenseapptype = (select id from egtl_mstr_app_type where name = 'New License'));

update eg_wf_state_history set sla = (createddate + interval '1 day' * (select value from egtl_configuration where key = 'RENEW_APPTYPE_DEFAULT_SLA') :: Integer) where state_id in (select state_id from egtl_license where licenseapptype = (select id from egtl_mstr_app_type where name = 'License Renewal'));

update eg_wf_state_history set sla = (createddate + interval '1 day' * (select value from egtl_configuration where key = 'CLOSURE_APPTYPE_DEFAULT_SLA') :: Integer) where state_id in (select state_id from egtl_license where licenseapptype = (select id from egtl_mstr_app_type where name = 'License Closure'));