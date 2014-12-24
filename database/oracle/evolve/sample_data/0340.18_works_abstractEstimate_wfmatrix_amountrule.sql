#UP

update eg_wf_matrix set fromqty=250000.1 where fromqty=250001 and objecttype='AbstractEstimate';

update eg_wf_matrix set fromqty=1000000.1 where fromqty=1000001 and objecttype='AbstractEstimate';

#DOWN

update eg_wf_matrix set fromqty=250001 where fromqty=250000.1 and objecttype='AbstractEstimate';

update eg_wf_matrix set fromqty=1000001 where fromqty=1000000.1 and objecttype='AbstractEstimate';

