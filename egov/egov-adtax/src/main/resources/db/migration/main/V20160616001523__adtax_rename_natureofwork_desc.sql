
update eg_wf_state_history set natureoftask  ='Advertisement' where state_id in (select id from eg_wf_states where type='AdvertisementPermitDetail') and natureoftask  ='CREATEADVERTISEMENT';

update eg_wf_states  set natureoftask  ='Advertisement'   where type='AdvertisementPermitDetail' and natureoftask  ='CREATEADVERTISEMENT';
