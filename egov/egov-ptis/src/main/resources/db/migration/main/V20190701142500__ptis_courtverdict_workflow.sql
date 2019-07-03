---COURT VERDICT WORK FLOW TYPE
INSERT INTO eg_wf_types(id, module, type, link, createdby, createddate, lastmodifiedby, lastmodifieddate, enabled, grouped, typefqn, displayname, version) VALUES (nextval('seq_eg_wf_types'), (SELECT id FROM eg_module WHERE name='Property Tax'), 'CourtVerdict', '/ptis/courtverdict/update/:ID', 1, now(), 1, now(), 'Y', 'N', 'org.egov.ptis.domain.entity.property.CourtVerdict', 'Court Verdict', 0);


---COURT VERDICT

delete from eg_wf_matrix where objecttype = 'CourtVerdict';

--Revenue Officer
INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (NEXTVAL('seq_eg_wf_matrix'), 'ANY', 'CourtVerdict', 'Revenue Officer Approved', NULL, 'Assistant Commissioner Approval Pending', 'Assistant Commissioner', NULL, 'Assistant Commissioner Approved', NULL, 'Zonal Commissioner,Deputy Commissioner,Additional Commissioner,Commissioner','Assistant Commissioner Approved', 'Forward,Approve,Reject', NULL, NULL, now(), '2099-04-01');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (NEXTVAL('seq_eg_wf_matrix'), 'ANY', 'CourtVerdict', 'Revenue Officer Approved', NULL, 'Zonal Commissioner Approval Pending', 'Zonal Commissioner', NULL, 'Zonal Commissioner Approved', NULL, 'Deputy Commissioner,Additional Commissioner,Commissioner','Assistant Commissioner Approved', 'Forward,Approve,Reject', NULL, NULL, now(), '2099-04-01');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (NEXTVAL('seq_eg_wf_matrix'), 'ANY', 'CourtVerdict', 'Revenue Officer Approved', NULL, 'Deputy Commissioner Approval Pending', 'Deputy Commissioner', NULL, 'Deputy Commissioner Approved', NULL, 'Additional Commissioner,Commissioner','Deputy Commissioner Approved', 'Forward,Approve,Reject', NULL, NULL, now(), '2099-04-01');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (NEXTVAL('seq_eg_wf_matrix'), 'ANY', 'CourtVerdict', 'Revenue Officer Approved', NULL, 'Additional Commissioner Approval Pending', 'Additional Commissioner', NULL, 'Additional Commissioner Approved', NULL, 'Commissioner','Additional Commissioner Approved', 'Forward,Approve,Reject', NULL, NULL, now(), '2099-04-01');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (NEXTVAL('seq_eg_wf_matrix'), 'ANY', 'CourtVerdict', 'Revenue Officer Approved', NULL, 'Commissioner Approval Pending', 'Commissioner', NULL, 'END', NULL, 'Commissioner','Commissioner Approved', 'Approve,Reject', NULL, NULL, now(), '2099-04-01');

--Assistant Commissioner
INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (NEXTVAL('seq_eg_wf_matrix'), 'ANY', 'CourtVerdict', 'Assistant Commissioner Approved', NULL, 'Zonal Commissioner Approval Pending', 'Zonal Commissioner', NULL, 'Zonal Commissioner Approved', NULL, 'Deputy Commissioner,Additional Commissioner,Commissioner','Assistant Commissioner Approved', 'Forward,Approve,Reject', NULL, NULL, now(), '2099-04-01');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (NEXTVAL('seq_eg_wf_matrix'), 'ANY', 'CourtVerdict', 'Assistant Commissioner Approved', NULL, 'Deputy Commissioner Approval Pending', 'Deputy Commissioner', NULL, 'Deputy Commissioner Approved', NULL, 'Additional Commissioner,Commissioner','Deputy Commissioner Approved', 'Forward,Approve,Reject', NULL, NULL, now(), '2099-04-01');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (NEXTVAL('seq_eg_wf_matrix'), 'ANY', 'CourtVerdict', 'Assistant Commissioner Approved', NULL, 'Additional Commissioner Approval Pending', 'Additional Commissioner', NULL, 'Additional Commissioner Approved', NULL, 'Commissioner','Additional Commissioner Approved', 'Forward,Approve,Reject', NULL, NULL, now(), '2099-04-01');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (NEXTVAL('seq_eg_wf_matrix'), 'ANY', 'CourtVerdict', 'Assistant Commissioner Approved', NULL, 'Commissioner Approval Pending', 'Commissioner', NULL, 'END', NULL, 'Commissioner','Commissioner Approved', 'Approve,Reject', NULL, NULL, now(), '2099-04-01');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (NEXTVAL('seq_eg_wf_matrix'), 'ANY', 'CourtVerdict', 'Assistant Commissioner Approved', NULL, NULL, 'Additional Commissioner', NULL, 'END', NULL, NULL, NULL, '', NULL, NULL, now(), '2099-04-01');

----Zonal Commissioner

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (NEXTVAL('seq_eg_wf_matrix'), 'ANY', 'CourtVerdict', 'Zonal Commissioner Approved', NULL, 'Deputy Commissioner Approval Pending', 'Deputy Commissioner', NULL, 'Deputy Commissioner Approved', NULL, 'Additional Commissioner,Commissioner',NULL, 'Forward,Approve,Reject', NULL, NULL, now(), '2099-04-01');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (NEXTVAL('seq_eg_wf_matrix'), 'ANY', 'CourtVerdict', 'Zonal Commissioner Approved', NULL, 'Additional Commissioner Approval Pending', 'Additional Commissioner', NULL, 'Additional Commissioner Approved', NULL, 'Commissioner',NULL, 'Forward,Approve,Reject', NULL, NULL, now(), '2099-04-01');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (NEXTVAL('seq_eg_wf_matrix'), 'ANY', 'CourtVerdict', 'Zonal Commissioner Approved', NULL, 'Commissioner Approval Pending', 'Commissioner', NULL, 'END', NULL, 'Commissioner','Commissioner Approved', 'Approve,Reject', NULL, NULL, now(), '2099-04-01');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (NEXTVAL('seq_eg_wf_matrix'), 'ANY', 'CourtVerdict', 'Zonal Commissioner Approved', NULL, NULL, 'Zonal Commissioner',NULL, 'END', NULL, NULL, NULL, '', NULL, NULL, now(), '2099-04-01');

----Deputy Commissioner
INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (NEXTVAL('seq_eg_wf_matrix'), 'ANY', 'CourtVerdict', 'Deputy Commissioner Approved', NULL, 'Additional Commissioner Approval Pending', 'Additional Commissioner', NULL, 'Additional Commissioner Approved', NULL, 'Commissioner','Additional Commissioner Approved', 'Forward,Approve,Reject', NULL, NULL, now(), '2099-04-01');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (NEXTVAL('seq_eg_wf_matrix'), 'ANY', 'CourtVerdict', 'Deputy Commissioner Approved', NULL, 'Commissioner Approval Pending', 'Commissioner', NULL, 'END', NULL, 'Commissioner','Commissioner Approved', 'Approve,Reject', NULL, NULL, now(), '2099-04-01');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (NEXTVAL('seq_eg_wf_matrix'), 'ANY', 'CourtVerdict', 'Deputy Commissioner Approved', NULL, NULL, 'Deputy Commissioner', NULL, 'END', NULL, NULL, NULL, '', NULL, NULL, now(), '2099-04-01');

--Additional Commissioner
INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (NEXTVAL('seq_eg_wf_matrix'), 'ANY', 'CourtVerdict', 'Additional Commissioner Approved', NULL, 'Commissioner Approval Pending', 'Commissioner', NULL, 'END', NULL, 'Commissioner','Commissioner Approved', 'Approve,Reject', NULL, NULL, now(), '2099-04-01');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (NEXTVAL('seq_eg_wf_matrix'), 'ANY', 'CourtVerdict', 'Additional Commissioner Approved', NULL, NULL, 'Additional Commissioner', NULL, 'END', NULL, NULL, NULL, '', NULL, NULL, now(), '2099-04-01');

----Rejection and common
INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (NEXTVAL('seq_eg_wf_matrix'), 'ANY', 'CourtVerdict', 'Rejected', NULL, 'Revenue Officer Approval Pending', 'Revenue Officer', NULL, 'Revenue Officer Approved', NULL, 'Assistant Commissioner,Zonal Commissioner,Deputy Commissioner,Additional Commissioner,Commissioner', 'Revenue Officer Approved', 'Forward,Reject', NULL, NULL, now(), '2099-04-01');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (NEXTVAL('seq_eg_wf_matrix'), 'ANY', 'CourtVerdict', 'Created', NULL, NULL, NULL, NULL, 'Revenue Officer Approved', NULL, 'Assistant Commissioner,Zonal Commissioner,Deputy Commissioner,Additional Commissioner,Commissioner', 'Revenue Officer Approved', 'Forward', NULL, NULL, now(), '2099-04-01');



