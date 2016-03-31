update eg_wf_matrix  set validactions='GENERATE DEMAND NOTICE,CANCEL RENEWAL' where objecttype='AdvertisementPermitDetail' and currentstate='Commissioner Approved'
and pendingactions='Collection pending' AND ADDITIONALRULE='RENEWADVERTISEMENT';

update eg_wf_matrix  set validactions='GENERATE DEMAND NOTICE,CANCEL APPLICATION' where objecttype='AdvertisementPermitDetail' and currentstate='Commissioner Approved'
and pendingactions='Collection pending' AND ADDITIONALRULE='CREATEADVERTISEMENT';