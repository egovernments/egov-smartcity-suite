#UP

update eg_wf_matrix set fromqty=0 where DEPARTMENT='Public Work' and OBJECTTYPE='AbstractEstimate' and additionalrule='HQBudgetApp'
and fromqty=50000.1 and toqty=250000;

update eg_wf_matrix set fromqty=0 where DEPARTMENT='Public Work' and OBJECTTYPE='AbstractEstimate' and additionalrule='HQDepositCodeApp'
and fromqty=50000.1 and toqty=250000;

#DOWN

update eg_wf_matrix set fromqty=50000.1 where DEPARTMENT='Public Work' and OBJECTTYPE='AbstractEstimate' and additionalrule='HQBudgetApp'
and fromqty=0 and toqty=250000;

update eg_wf_matrix set fromqty=50000.1 where DEPARTMENT='Public Work' and OBJECTTYPE='AbstractEstimate' and additionalrule='HQDepositCodeApp'
and fromqty=0 and toqty=250000;
