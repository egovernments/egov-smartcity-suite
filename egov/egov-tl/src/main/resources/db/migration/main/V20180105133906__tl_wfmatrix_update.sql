update eg_wf_matrix set additionalrule='NEWLICENSECOLLECTION' where currentstate='Sanitary Supervisor Verified' and currentdesignation='Chief Medical Officer of Health' and additionalrule = 'NEWLICENSE' and nextaction='Second Level Collection Pending';

update eg_wf_matrix set additionalrule='RENEWLICENSECOLLECTION' where currentstate='Sanitary Supervisor Verified' and currentdesignation='Chief Medical Officer of Health' and additionalrule = 'RENEWLICENSE' and nextaction='Second Level Collection Pending';

update eg_wf_matrix set nextaction='Digital Signature Pending' where objecttype='TradeLicense' and additionalrule like '%NEWLICENSE' and currentstate='Sanitary Supervisor Verified' and currentdesignation='Assistant Medical Officer of Health';

update eg_wf_matrix set nextaction='Digital Signature Pending' where objecttype='TradeLicense' and additionalrule like '%NEWLICENSE' and currentstate='Sanitary Supervisor Verified' and currentdesignation='Chief Medical Officer of Health';
