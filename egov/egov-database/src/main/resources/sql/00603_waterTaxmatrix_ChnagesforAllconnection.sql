
 INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY',
 'WaterConnectionDetails', 'Estmate Notice Generated', NULL, NULL, 'Revenue Clerk', 
 'NEWCONNECTION', 'Payment done against Estimation', 'Ready For Payment', 'Commissioner', 
'Payment against Estimation', 'Submit,Reject',
 NULL, NULL, '2015-08-01', '2099-04-01');

INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY',
 'WaterConnectionDetails', 'Estmate Notice Generated', NULL, NULL, 'Revenue Clerk', 
 'ADDNLCONNECTION', 'Payment done against Estimation', 'Ready For Payment', 'Commissioner', 
'Payment against Estimation', 'Submit,Reject',
 NULL, NULL, '2015-08-01', '2099-04-01');

INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY',
 'WaterConnectionDetails', 'Estmate Notice Generated', NULL, NULL, 'Revenue Clerk', 
 'CHANGEOFUSE', 'Payment done against Estimation', 'Ready For Payment', 'Commissioner', 
'Payment against Estimation', 'Submit,Reject',
 NULL, NULL, '2015-08-01', '2099-04-01');
 

 update eg_wf_matrix set nextstate='Estmate Notice Generated',nextdesignation='Revenue Clerk',
  nextstatus='Ready for Payment', validactions='Generate Estimation Notice' where id in(select id from eg_wf_matrix where
   currentstate='Asst engg approved' and objecttype='WaterConnectionDetails' and additionalrule='ADDNLCONNECTION' );

 update eg_wf_matrix set nextstate='Estmate Notice Generated',nextdesignation='Revenue Clerk',
  nextstatus='Ready for Payment', validactions='Generate Estimation Notice' where id in(select id from eg_wf_matrix where
   currentstate='Asst engg approved' and objecttype='WaterConnectionDetails' and additionalrule='NEWCONNECTION' );

update eg_wf_matrix set nextstate='Estmate Notice Generated',nextdesignation='Revenue Clerk',
  nextstatus='Ready for Payment', validactions='Generate Estimation Notice' where id in(select id from eg_wf_matrix where
   currentstate='Asst engg approved' and objecttype='WaterConnectionDetails' and additionalrule='CHANGEOFUSE' );

