update eg_wf_states set nextaction=null where type='AdvertisementPermitDetail' and value ='NEW' and nextaction='Junior/Senior Assistance approval pending';

update eg_wf_state_history  set nextaction=null where natureoftask='Advertisement' and value ='NEW' and nextaction like 'Junior/Senior Assistance approval pending';
