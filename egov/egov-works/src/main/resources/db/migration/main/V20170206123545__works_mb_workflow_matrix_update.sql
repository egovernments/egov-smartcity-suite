update eg_wf_matrix set currentdesignation  = '' where objecttype = 'MBHeader';
update eg_wf_matrix set nextdesignation  = 'Deputy Executive Engineer' where objecttype = 'MBHeader' and additionalrule='NP' and fromqty=0;
update eg_wf_matrix set nextdesignation  = 'Deputy Executive Engineer' where objecttype = 'MBHeader' and additionalrule='III' and fromqty=0;