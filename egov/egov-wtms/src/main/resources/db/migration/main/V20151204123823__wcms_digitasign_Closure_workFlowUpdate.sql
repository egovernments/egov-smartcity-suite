
update eg_wf_matrix set nextstate='Closure Digital Sign Updated' where objecttype='WaterConnectionDetails' and additionalrule='CLOSECONNECTION'
and currentstate='Closure Approved By Commissioner';