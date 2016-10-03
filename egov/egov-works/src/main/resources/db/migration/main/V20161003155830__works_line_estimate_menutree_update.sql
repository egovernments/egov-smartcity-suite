update eg_module set displayname = 'Estimate' where name = 'WorksLineEstimate' and parentmodule = (select id from eg_module where name = 'Works Management');
update eg_action set displayname = 'Create Estimate' where name = 'WorksCreateLineEstimateNewForm' and parentmodule = (select id from eg_module  where name = 'WorksLineEstimate');
update eg_action set displayname = 'Search Estimate' where name = 'WorksSearchLineEstimateForm' and parentmodule = (select id from eg_module  where name = 'WorksLineEstimate');
update eg_action set displayname = 'Create Spillover Estimate' where name = 'WorksSpillOverLineEstimateForm' and parentmodule = (select id from eg_module  where name = 'WorksLineEstimate');
update eg_action set displayname = 'Cancel Estimate' where name = 'SearchLineEstimateToCancelForm' and parentmodule = (select id from eg_module  where name = 'WorksAdministrator');
update eg_wf_states set natureoftask = 'Estimate' where type='LineEstimate' and natureoftask  ='Line Estimate';

--rollback update eg_module set displayname = 'Line Estimate' where name = 'WorksLineEstimate' and parentmodule = (select id from eg_module where name = 'Works Management');
--rollback update eg_action set displayname = 'Create Line Estimate' where name = 'WorksCreateLineEstimateNewForm' and parentmodule = (select id from eg_module  where where name = 'WorksLineEstimate');
--rollback update eg_action set displayname = 'Search Line Estimate' where name = 'WorksSearchLineEstimateForm' and parentmodule = (select id from eg_module  where name = 'WorksLineEstimate');
--rollback update eg_action set displayname = 'Create Spillover Line Estimate' where name = 'WorksSpillOverLineEstimateForm' and parentmodule = (select id from eg_module  where name = 'WorksLineEstimate');
--rollback update eg_action set displayname = 'Cancel Line Estimate' where name = 'SearchLineEstimateToCancelForm' and parentmodule = (select id from eg_module  where name = 'WorksAdministrator');
--rollback update eg_wf_states set natureoftask = 'Line Estimate' where type='LineEstimate' and natureoftask  ='Estimate';
