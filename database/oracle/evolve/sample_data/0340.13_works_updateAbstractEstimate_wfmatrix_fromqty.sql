#UP

update eg_wf_matrix set fromqty=0 where fromqty=50000 and objecttype='AbstractEstimate'

#DOWN

update eg_wf_matrix set fromqty=50000 where fromqty=0 and objecttype='AbstractEstimate'
