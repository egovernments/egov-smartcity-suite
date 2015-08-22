update eg_wf_matrix set nextaction='Ready For Payment'  where id in(select id from eg_wf_matrix where
   currentstate='Asst engg approved' and objecttype='WaterConnectionDetails' and additionalrule='ADDNLCONNECTION' );

 update eg_wf_matrix set nextaction='Ready For Payment'  where id in(select id from eg_wf_matrix where
   currentstate='Asst engg approved' and objecttype='WaterConnectionDetails' and additionalrule='NEWCONNECTION' );

update eg_wf_matrix set nextaction='Ready For Payment'  where id in(select id from eg_wf_matrix where
   currentstate='Asst engg approved' and objecttype='WaterConnectionDetails' and additionalrule='CHANGEOFUSE' );
