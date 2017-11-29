-------- Change In CLoset : To forward application from DEE to EE if demand is reduced --------------

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate,version) VALUES
(nextval('seq_eg_wf_matrix'),'ANY','SewerageApplicationDetails','Assistant Engineer Approved',NULL,'Deputy Exe Engineer Approval Pending','Deputy Executive Engineer','CHANGEINCLOSETS NOCOLLECTION','Deputy Exe Engineer Approved','Executive Engineer Approval Pending','Executive Engineer','DEEAPPROVED','Forward,Reject', NULL, NULL,now(),now(),'0');

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate,version) VALUES
(nextval('seq_eg_wf_matrix'),'ANY','SewerageApplicationDetails','Deputy Exe Engineer Approved',NULL,'Executive Engineer Approval Pending','Executive Engineer','CHANGEINCLOSETS NOCOLLECTION','Executive Engineer Approved','Work Order Generation Pending','Senior Assistant,Junior Assistant','FINALAPPROVED','Approve', NULL, NULL,now(),now(),'0');

