#UP

update eg_wf_matrix set fromqty=50000.1 where toqty=250000 and objecttype='AbstractEstimate' and department='Electrical';

update eg_wf_matrix set fromqty=50000.1 where toqty=250000 and objecttype='AbstractEstimate' and department='Water Works';

update eg_wf_matrix set fromqty=50000.1 where toqty=500000 and objecttype='AbstractEstimate' and department='Garden';

update eg_wf_matrix set fromqty=50000.1 where toqty=500000 and objecttype='AbstractEstimate' and department='Public Work';

#DOWN

update eg_wf_matrix set fromqty=0 where toqty=250000 and objecttype='AbstractEstimate' and department='Electrical';

update eg_wf_matrix set fromqty=0 where toqty=250000 and objecttype='AbstractEstimate' and department='Water Works';

update eg_wf_matrix set fromqty=0 where toqty=500000 and objecttype='AbstractEstimate' and department='Garden';

update eg_wf_matrix set fromqty=0 where toqty=500000 and objecttype='AbstractEstimate' and department='Public Work';


