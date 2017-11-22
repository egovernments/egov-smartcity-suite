update eg_wf_matrix set currentstate='Start' where objecttype = 'TradeLicense' and additionalrule in ('NEWLICENSE','RENEWLICENSE') and currentstate='New';

