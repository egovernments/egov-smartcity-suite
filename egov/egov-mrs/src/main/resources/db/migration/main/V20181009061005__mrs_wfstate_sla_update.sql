update eg_wf_states set sla=(createddate + interval '1 day' * (select value from eg_appconfig_values  where key_id =(select id from eg_appconfig  where key_name ='SLAFORMARRIAGEREGISTRATION')):: Integer) where id in (select state_id from egmrs_registration);

update eg_wf_state_history set sla=(createddate + interval '1 day' * (select value from eg_appconfig_values  where key_id =(select id from eg_appconfig  where key_name ='SLAFORMARRIAGEREGISTRATION')):: Integer) where state_id in (select state_id from egmrs_registration);

update eg_wf_states set sla=(createddate + interval '1 day' * (select value from eg_appconfig_values  where key_id =(select id from eg_appconfig  where key_name ='SLAFORMARRIAGEREISSUE')) :: Integer) where id in (select state_id from egmrs_reissue);

update eg_wf_state_history set sla=(createddate + interval '1 day' * (select value from eg_appconfig_values  where key_id =(select id from eg_appconfig  where key_name ='SLAFORMARRIAGEREGISTRATION')):: Integer) where state_id in (select state_id from egmrs_reissue);