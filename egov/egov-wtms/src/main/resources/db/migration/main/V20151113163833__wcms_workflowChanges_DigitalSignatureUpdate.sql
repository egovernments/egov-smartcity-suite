
update eg_wf_matrix set currentdesignation='Commissioner',nextstate='Commissioner Approved',
   nextaction='Digital Signature Pending',nextstatus='Digital Signature Pending',nextDesignation='Commissioner' 
   where currentstate='Payment done against Estimation';

update eg_wf_matrix set currentstate='Digital Signature Updated',nextstate='Work order generated',
   nextaction='Tap execution pending'
   where currentstate='Commissioner Approved'; 

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions,
 currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, 
 fromqty, toqty, fromdate, todate) VALUES (nextval('eg_wf_matrix_seq'), 'ANY', 'WaterConnectionDetails', 
 'Commissioner Approved', NULL, NULL, 'Commissioner', 'NEWCONNECTION', 'Digital Signature Updated',
  'WorkOrder Print Pending', 'Senior Assistant,Junior Assistant', 'Digital Signature Pending', 'Forward', NULL, NULL,
   '2015-08-01', '2099-04-01');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions,
 currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, 
 fromqty, toqty, fromdate, todate) VALUES (nextval('eg_wf_matrix_seq'), 'ANY', 'WaterConnectionDetails', 
 'Commissioner Approved', NULL, NULL, 'Commissioner', 'ADDNLCONNECTION', 'Digital Signature Updated',
  'WorkOrder Print Pending', 'Senior Assistant,Junior Assistant', 'Digital Signature Pending', 'Forward', NULL, NULL,
   '2015-08-01', '2099-04-01');
	
INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions,
 currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, 
 fromqty, toqty, fromdate, todate) VALUES (nextval('eg_wf_matrix_seq'), 'ANY', 'WaterConnectionDetails', 
 'Commissioner Approved', NULL, NULL, 'Commissioner', 'CHANGEOFUSE', 'Digital Signature Updated',
  'WorkOrder Print Pending', 'Senior Assistant,Junior Assistant', 'Digital Signature Pending', 'Forward', NULL, NULL,
   '2015-08-01', '2099-04-01');

update eg_wf_matrix set nextstate='Closure Approved By Commissioner',nextaction='Digital Signature Pending',nextstatus='Digital Signature Pending',nextDesignation='Commissioner' 
where currentstate='Close approve By Comm'; 

update eg_wf_matrix set currentstate='Closure Digital Sign Updated' where objecttype='WaterConnectionDetails' and additionalrule='CLOSECONNECTION' and currentstate='Generate Acknowledgemnt';

update eg_wf_matrix set nextstate='Closure Digital Sign Updated' where objecttype='WaterConnectionDetails' and additionalrule='CLOSECONNECTION' and currentstate='Closure Approved By Commissioner';
INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions,
 currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, 
 fromqty, toqty, fromdate, todate) VALUES (nextval('eg_wf_matrix_seq'), 'ANY', 'WaterConnectionDetails', 
 'Closure Approved By Commissioner', NULL, NULL, 'Commissioner', 'CLOSECONNECTION', 'Generate Acknowledgemnt',
  'acknowledgemnt pending', 'Senior Assistant,Junior Assistant', 'acknowledgemnt pending', 'Forward', NULL, NULL,
   '2015-08-01', '2099-04-01');

update eg_wf_matrix set nextstate='ReConn Approved By Commissioner',nextaction='Digital Signature Pending'
   ,nextstatus='Digital Signature Pending',nextDesignation='Commissioner' 
   where currentstate='Reconnection approve By Comm'; 

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions,
 currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, 
 fromqty, toqty, fromdate, todate) VALUES (nextval('eg_wf_matrix_seq'), 'ANY', 'WaterConnectionDetails', 
'ReConn Approved By Commissioner', NULL, NULL, 'Commissioner', 'RECONNECTION', 'Reconnection Acknowledgemnt',
'Acknowledgemnt Print pending', 'Senior Assistant,Junior Assistant', 'Acknowledgemnt Print pending', 'Forward', NULL, NULL,
'2015-08-01', '2099-04-01');


update eg_wf_matrix set currentstate='ReConn Digital Sign Updated' where objecttype='WaterConnectionDetails' and 
additionalrule='RECONNECTION' and currentstate='Reconnection Acknowledgemnt';

update eg_wf_matrix set nextstate='ReConn Digital Sign Updated' where objecttype='WaterConnectionDetails' and 
additionalrule='RECONNECTION' and currentstate='ReConn Approved By Commissioner';

Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID)
 values (nextval('SEQ_EGW_STATUS'),'WATERTAXAPPLICATION','Digital Signature Updated',now(),'DIGITALSIGNATUREUPDATED',1);

Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID)
 values (nextval('SEQ_EGW_STATUS'),'WATERTAXAPPLICATION','Closure Conn Digital Signature Updated',now(),'CLOSUREDIGSIGNUPDATED',1);

Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID)
 values (nextval('SEQ_EGW_STATUS'),'WATERTAXAPPLICATION','ReConnection Digital Signature Updated',now(),'RECONNDIGSIGNUPDATED',1);

insert into eg_roleaction (roleid,actionid )values((select id from eg_role where name='Property Verifier'),
   (select id from eg_action where url='/application/workorder' and contextroot='wtms'));

insert into eg_roleaction (roleid,actionid )values((select id from eg_role where name='Property Verifier'),
   (select id from eg_action where url='/application/acknowlgementNotice' and contextroot='wtms'));

insert into eg_roleaction (roleid,actionid )values((select id from eg_role where name='Property Verifier'),
   (select id from eg_action where url='/application/ReconnacknowlgementNotice' and contextroot='wtms'));
