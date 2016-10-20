update eg_module set displayname = 'Abstract/Detailed Estimate' where name = 'WorksAbstractEstimate' and contextroot = 'egworks';
update eg_action set displayname = 'Create Abstract/Detailed Estimate' where name = 'WorksSearchLineEstimatesToCreateAE' and contextroot = 'egworks';
update eg_action set displayname = 'Search Abstract/Detailed Estimate' where name = 'AbstractEstimateSearchForm' and contextroot = 'egworks';
update eg_action set displayname = 'Cancel Abstract/Detailed Estimate' where name = 'SearchEstimateToCancelForm' and contextroot = 'egworks';
update eg_wf_states set natureoftask = 'Abstract/Detailed Estimate' where type='AbstractEstimate' and natureoftask  ='Abstract Estimate';

update eg_wf_types set displayname = 'Abstract/Detailed Estimate' where type='AbstractEstimate';
update eg_wf_state_history set natureoftask = 'Abstract/Detailed Estimate' where state_id in (select id from eg_wf_states where  type='AbstractEstimate');

update eg_action set displayname = 'Offline status for Abstract/Detailed Estimate' where name = 'WorksSearchAbstractEstimateToSetOfflineStattus' and contextroot = 'egworks';

--rollback update eg_wf_types set displayname = 'Abstract Estimate' where type='AbstractEstimate';

--rollback update eg_module set displayname = 'Abstract Estimate' where name = 'WorksAbstractEstimate' and contextroot = 'egworks';
--rollback update eg_action set displayname = 'Create Abstract Estimate' where name = 'WorksSearchLineEstimatesToCreateAE' and contextroot = 'egworks';
--rollback update eg_action set displayname = 'Search Abstract Estimate' where name = 'AbstractEstimateSearchForm' and contextroot = 'egworks';
--rollback update eg_action set displayname = 'Cancel Abstract Estimate' where name = 'SearchEstimateToCancelForm' and contextroot = 'egworks';
--rollback update eg_wf_states set natureoftask = 'Abstract Estimate' where type='AbstractEstimate' and natureoftask  ='Abstract Estimate';
--rollback update eg_action set displayname = 'Offline status for Abstract Estimate' where name = 'WorksSearchAbstractEstimateToSetOfflineStattus' and contextroot = 'egworks';