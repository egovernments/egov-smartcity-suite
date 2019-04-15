update eg_wf_matrix set currentdesignation = 'Executive Engineer',nextdesignation = 'Executive Engineer' where objecttype = 'SewerageApplicationDetails' and additionalrule = 'NEWSEWERAGECONNECTION' and currentstate = 'Deputy Exe Engineer Approved' and currentdesignation = 'Senior Assistant,Junior Assistant' and nextdesignation = 'Senior Assistant,Junior Assistant';

delete from eg_wf_matrix where objecttype = 'SewerageApplicationDetails' and additionalrule = 'NEWSEWERAGECONNECTION' and currentstate = 'Deputy Exe Engineer Approved' and nextstate = 'Payment Done Against Estimation';
