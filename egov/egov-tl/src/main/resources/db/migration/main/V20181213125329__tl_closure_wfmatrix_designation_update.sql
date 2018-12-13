update eg_wf_matrix set currentdesignation = 'Sanitary Inspector' where additionalrule like '%CLOSURE%' and currentdesignation='Sanitary inspector';
update eg_wf_matrix set nextdesignation = 'Sanitary Inspector' where  additionalrule like '%CLOSURE%' and nextdesignation='Sanitary inspector';
