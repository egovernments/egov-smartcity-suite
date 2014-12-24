#UP

update eg_wf_matrix set fromqty=1000000.1 where objecttype='AbstractEstimate' and department='Water Works' and additionalrule in ('budgetApp','depositCodeApp') and fromqty=100000.1; 

#DOWN

update eg_wf_matrix set fromqty=100000.1 where objecttype='AbstractEstimate' and department='Water Works' and additionalrule in ('budgetApp','depositCodeApp') and fromqty=1000000.1; 


