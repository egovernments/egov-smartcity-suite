--------------------EG_WF_MATRIX-------------------
INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate,version) VALUES
(nextval('seq_eg_wf_matrix'),'ANY','SewerageApplicationDetails','NEW', NULL, NULL,'Senior Assistant,Junior Assistant','CLOSESEWERAGECONNECTION','Clerk Approved','Assistant Engineer Approval Pending','Assistant Engineer','CREATED','Forward', NULL, NULL,now(),now(),'0');

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate,version) VALUES
(nextval('seq_eg_wf_matrix'),'ANY','SewerageApplicationDetails','Clerk Approved',NULL,'Assistant Engineer Approval Pending','Assistant Engineer','CLOSESEWERAGECONNECTION','Assistant Engineer Approved','Deputy Exe Engineer Approval Pending','Deputy Executive Engineer','INITIALAPPROVED','Forward,Reject', NULL, NULL,now(),now(),'0');

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate,version) VALUES
(nextval('seq_eg_wf_matrix'),'ANY','SewerageApplicationDetails','Assistant Engineer Approved',NULL,'Deputy Exe Engineer Approval Pending','Deputy Executive Engineer','CLOSESEWERAGECONNECTION','Deputy Exe Engineer Approved','Executive Engineer Approval Pending','Executive Engineer','DEEAPPROVED','Forward,Reject', NULL, NULL,now(),now(),'0');

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate,version) VALUES
(nextval('seq_eg_wf_matrix'),'ANY','SewerageApplicationDetails','Deputy Exe Engineer Approved',NULL,'Executive Engineer Approval Pending','Executive Engineer','CLOSESEWERAGECONNECTION','Executive Engineer Approved','Close Connection Notice Generation Pending','Senior Assistant,Junior Assistant','FINALAPPROVED','Approve', NULL, NULL,now(),now(),'0');

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate,version) VALUES
(nextval('seq_eg_wf_matrix'),'ANY','SewerageApplicationDetails','Executive Engineer Approved',NULL,'Close Connection Notice Generation Pending','Senior Assistant,Junior Assistant','CLOSESEWERAGECONNECTION','END','END',NULL,NULL,'Generate Close Connection Notice', NULL, NULL,now(),now(),'0');


INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate,version) VALUES
(nextval('seq_eg_wf_matrix'),'ANY','SewerageApplicationDetails','Rejected',NULL,'Rejected','Senior Assistant,Junior Assistant','CLOSESEWERAGECONNECTION','Clerk Approved','Assistant Engineer Approval Pending','Assistant Engineer','CREATED','Forward,Cancel', NULL, NULL,now(),now(),'0');
