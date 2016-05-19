update eg_wf_types set typefqn='org.egov.works.abstractestimate.entity.AbstractEstimate'  where type = 'AbstractEstimate';

--rollback pdate eg_wf_types set typefqn='org.egov.works.models.estimate.AbstractEstimate'  where type = 'AbstractEstimate';