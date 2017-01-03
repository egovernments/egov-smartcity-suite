update eg_wf_matrix set nextdesignation='Additional Commissioner,Commissioner' where objecttype='RevisionPetition' and additionalrule='REVISION PETITION' and currentdesignation='Deputy Commissioner' and nextstate='RP:Deputy Commissioner Approved' and nextaction='Print Endoresement' and currentstate='RP:Zonal Commissioner Forwarded';

update eg_wf_matrix set nextdesignation='Additional Commissioner,Commissioner' where objecttype='RevisionPetition' and additionalrule='GENERAL REVISION PETITION' and currentdesignation='Deputy Commissioner' and nextstate='GRP:Deputy Commissioner Approved' and nextaction='Print Endoresement'and currentstate='GRP:Zonal Commissioner Forwarded';

