update eg_wf_matrix set rejectenabled  = null where objecttype='TradeLicense';

update eg_wf_matrix set rejectenabled =true where objecttype='TradeLicense' and additionalrule  in('NEWLICENSE','NEWLICENSECOLLECTION','NEWLICENSEREJECT','RENEWLICENSE','RENEWLICENSECOLLECTION','RENEWLICENSEREJECT') and currentdesignation in ('Senior Assistant,Junior Assistant','Sanitary Inspector') ;

