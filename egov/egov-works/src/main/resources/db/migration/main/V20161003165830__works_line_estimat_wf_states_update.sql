update eg_wf_types set displayname = 'Estimate' where type='LineEstimate';
update eg_wf_state_history set natureoftask = 'Estimate' where state_id in (select id from eg_wf_states where  type='LineEstimate');

--rollback update eg_wf_types set displayname = 'Line Estimate' where type='LineEstimate';
