update eg_wf_matrix set enablefields='tradeArea_weight,parentBoundary,boundary' where additionalrule in ('NEWLICENSE','NEWLICENSECOLLECTION','RENEWLICENSECOLLECTION') and currentstate in ('First Level Fee Collected','Sanitary Inspector Rejected') and enablefields='all';
update eg_wf_matrix set enablefields ='tradeArea_weight,parentBoundary,boundary,adminWard' where enablefields='tradeArea_weight,parentBoundary,boundary';
update eg_wf_matrix set enablefields ='parentBoundary,boundary,adminWard' where enablefields='parentBoundary,boundary';

