delete from eg_wf_matrix where objecttype ='WaterConnectionDetails' and additionalrule  in ('NEWCONNECTION','ADDNLCONNECTION','CHANGEOFUSE') and currentstate in ('Digital Signature Updated','Work order generated');
delete from eg_wf_matrix where objecttype ='WaterConnectionDetails' and additionalrule in ('NEWCONNECTION','ADDNLCONNECTION','CHANGEOFUSE') and nextstate in ('Digital Signature Updated','Work order generated');

