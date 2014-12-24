#UP

update egf_budgetdetail set approvedamount=originalamount;

#DOWN

update egf_budgetdetail set approvedamount=0;