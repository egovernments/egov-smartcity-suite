update eg_wf_matrix set nextaction='Digital Signature Pending' where objecttype='TradeLicense' and currentstate='Assistant Medical Officer of Health Approved' and nextstate='Commissioner Approved' and additionalrule in ('NEWLICENSE','RENEWLICENSE');

update eg_wf_matrix set nextaction='Digital Signature Pending' where objecttype='TradeLicense' and currentstate='Chief Medical Officer of Health Approved' and nextstate='Commissioner Approved' and additionalrule in ('NEWLICENSE','RENEWLICENSE');
