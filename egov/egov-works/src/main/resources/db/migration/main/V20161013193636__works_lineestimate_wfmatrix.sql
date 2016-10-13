delete from eg_wf_matrix where objecttype = 'LineEstimate';

--0 to 200000
INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction,nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'LineEstimate', 'NEW', NULL, NULL, 
'Assistant engineer', null,'Created', 'Pending Technical Approve', 'Deputy Executive Engineer', 'Created', 'Forward', 0, 200000, now(), now());

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction,nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'LineEstimate', 'Created', NULL, 'Pending Technical Approve', 
'Deputy Executive Engineer', null,'Technically Approved', 'Pending budgetery sanction', 'Junior Accountant,Senior Accountant,Accountant Category - IV,Junior Accounts Officer,Accounts Officer', 'Technically Approved', 'Submit,Reject', 0, 200000, now(), now());

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction,nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'LineEstimate', 'Rejected', NULL, NULL, 
'Assistant engineer', null, 'Resubmitted', 'Pending Technical Approve', 'Deputy Executive Engineer', 'Resubmitted', 'Forward,Cancel', 0, 200000, now(), now());

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction,nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'LineEstimate', 'Resubmitted', NULL, 'Pending Technical Approve',
'Deputy Executive Engineer', null, 'Technically Approved', 'Pending budgetery sanction', 'Junior Accountant,Senior Accountant,Accountant Category - IV,Junior Accounts Officer,Accounts Officer','Technically Approved', 'Submit,Reject', 0, 200000, now(), now());

--200000 to 1000000
INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction,nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'LineEstimate', 'NEW', NULL, NULL, 
'Assistant engineer', null, 'Created', 'Pending Check By Deputy Executive Engineer', 'Deputy Executive Engineer', 'Created', 'Forward', 200000.1, 1000000, now(), now());

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction,nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'LineEstimate', 'Created', NULL, 'Pending Check By Deputy Executive Engineer', 
'Deputy Executive Engineer', null, 'Checked', 'Pending Technical Approve', 'Executive Engineer', 'Checked', 'Submit,Reject', 200000.1, 1000000, now(), now());

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction,nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'LineEstimate', 'Checked By Deputy Executive Engineer', NULL, 'Pending Technical Approve', 
'Executive Engineer', null, 'Technically Approved', 'Pending budgetery sanction', 'Junior Accountant,Senior Accountant,Accountant Category - IV,Junior Accounts Officer,Accounts Officer', 'Technically Approved', 'Submit,Reject', 200000.1, 1000000, now(), now());


INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction,nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'LineEstimate', 'Rejected', NULL, NULL, 
'Assistant engineer', null, 'Resubmitted', 'Pending Check By Deputy Executive Engineer', 'Deputy Executive Engineer', 'Resubmitted', 'Forward,Cancel', 200000.1, 1000000, now(), now());

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction,nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'LineEstimate', 'Resubmitted', NULL, 'Pending Check By Deputy Executive Engineer', 
'Deputy Executive Engineer', null, 'Checked', 'Pending Technical Approve', 'Executive Engineer', 'Checked', 'Submit,Reject', 200000.1, 1000000, now(), now());


--1000000 to 5000000
INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction,nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'LineEstimate', 'NEW', NULL, NULL, 
'Assistant engineer', null, 'Created', 'Pending Check By Deputy Executive Engineer', 'Deputy Executive Engineer', 'Created', 'Forward', 1000000.1, 5000000, now(), now());

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction,nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'LineEstimate', 'Created', NULL, 'Pending Check By Deputy Executive Engineer', 
'Deputy Executive Engineer', null, 'Checked', 'Pending Check By Executive Engineer', 'Executive Engineer', 'Checked', 'Submit,Reject', 1000000.1, 5000000, now(), now());

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction,nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'LineEstimate', 'Checked', NULL, 'Pending Check By Executive Engineer', 
'Executive engineer', null, 'Checked', 'Pending Technical Approve', 'Superintending Engineer', 'Checked', 'Submit,Reject', 1000000.1, 5000000, now(), now());

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction,nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'LineEstimate', 'Checked', NULL, 'Pending Technical Approve', 
'Superintending Engineer', null, 'Technically Approved', 'Pending budgetery sanction', 'Junior Accountant,Senior Accountant,Accountant Category - IV,Junior Accounts Officer,Accounts Officer', 'Technically Approved', 'Submit,Reject', 1000000.1, 5000000, now(), now());

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction,nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'LineEstimate', 'Rejected', NULL, NULL, 
'Assistant engineer', null, 'Resubmitted', 'Pending Checked By Deputy Executive Engineer', 'Deputy Executive Engineer', NULL, 'Forward,Cancel', 1000000.1, 5000000, now(), now());

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction,nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'LineEstimate', 'Resubmitted', NULL, 'Pending Checked By Executive Engineer', 
'Deputy Executive Engineer', null, 'Checked', 'Pending Checked By Executive Engineer', 'Executive Engineer','Checked', 'Submit,Reject', 1000000.1, 5000000, now(), now());


-- >5000000
INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction,nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'LineEstimate', 'NEW', NULL, NULL, 
'Assistant engineer', null, 'Created', 'Pending Check By Deputy Executive Engineer', 'Deputy Executive Engineer', 'Created', 'Forward', 5000000.1, null, now(), now());

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction,nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'LineEstimate', 'Created', NULL, 'Pending Check By Deputy Executive Engineer', 
'Deputy Executive Engineer', null, 'Checked', 'Pending Check By Executive Engineer', 'Executive Engineer', 'Checked', 'Submit,Reject', 5000000.1, null, now(), now());

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction,nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'LineEstimate', 'Checked', NULL, 'Pending Check By Executive Engineer', 
'Executive engineer', null, 'Checked', 'Pending Check By Superintending Engineer', 'Superintending Engineer', 'Checked', 'Submit,Reject', 5000000.1, null, now(), now());

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction,nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'LineEstimate', 'Checked', NULL, 'Pending Check By Superintending Engineer', 
'Superintending Engineer', null, 'Checked', 'Pending Technical Approve', 'Chief Engineer', 'Checked', 'Submit,Reject', 5000000.1, null, now(), now());

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction,nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'LineEstimate', 'Checked', NULL, 'Pending Technical Approve', 
'Chief Engineer', null, 'Technically Approved', 'Pending budgetery sanction', 'Junior Accountant,Senior Accountant,Accountant Category - IV,Junior Accounts Officer,Accounts Officer', 'Technically Approved', 'Submit,Reject', 5000000.1, null, now(), now());

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction,nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'LineEstimate', 'Rejected', NULL, NULL, 
'Assistant engineer', null, 'Resubmitted', 'Pending Check By Deputy Executive Engineer', 'Deputy Executive Engineer', null, 'Forward,Cancel', 5000000.1, null, now(), now());

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction,nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'LineEstimate', 'Resubmitted', NULL, 'Pending Check By Deputy Executive Engineer', 
'Deputy Executive Engineer', null, 'Checked', 'Pending Check By Executive Engineer', 'Executive Engineer', 'Checked', 'Submit,Reject', 5000000.1, null, now(), now());

----Common budget and admin saction
INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction,nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'LineEstimate', 'Technically Approved', NULL, 'Pending budgetery sanction', 
'Junior Accountant,Senior Accountant,Accountant Category - IV,Junior Accounts Officer,Accounts Officer', null, 'Budget Sanctioned', 'Pending Admin Sanction', 'Commissioner', 'Budget Sanctioned', 'Submit,Reject', 0, null, now(), now());

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction,nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'LineEstimate', 'Budget Sanctioned', NULL, 'Pending Admin Sanction', 
'Commissioner', null, 'END', 'END', null, null, 'Approve,Reject', 0, null, now(), now());

--egw_status---
Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (nextval('SEQ_EGW_STATUS'),'LINEESTIMATE','Technically Approved',now(),'TECHNICALLY_APPROVED',3);

--eg_script--
INSERT INTO EG_SCRIPT VALUES(nextval('SEQ_EG_SCRIPT'),'LINEESTIMATE-APPROVALRULES','nashorn',1,now(),1,now(),
'function getWorkFlowFields() {
      var map = new java.util.HashMap();
      if(cityGrade.equals("Corp")) {
         if(estimateAmount <= 1000000) {
           map.put("noFieldsRequired",true);
         } else if(estimateAmount > 1000000 && estimateAmount <= 5000000) {
           map.put("standingCommitteeDetailsRequired",true);
         } else if(estimateAmount > 5000000 && estimateAmount <= 20000000) {
           map.put("councilResolutionDetailsRequired",true);
           map.put("standingCommitteeDetailsRequired",true);
         } else if(estimateAmount > 20000000) {
           map.put("governmentApprovalRequired",true);
           map.put("councilResolutionDetailsRequired",true);
           map.put("standingCommitteeDetailsRequired",true);
         }
      } else if(cityGrade.equals("I")) {
         if(estimateAmount <= 100000) {
          map.put("noFieldsRequired",true);
         } else if(estimateAmount > 100000 && estimateAmount <= 200000) {
          map.put("contractCommitteeDetailsRequired",true);
         } else if(estimateAmount > 200000) {
          map.put("contractCommitteeDetailsRequired",true);
          map.put("councilResolutionDetailsRequired",true);
         }
      } else if(cityGrade.equals("II")) {
         if(estimateAmount <= 50000) {
          map.put("noFieldsRequired",true);
         } else if(estimateAmount > 50000 && estimateAmount <= 100000) {
          map.put("contractCommitteeDetailsRequired",true);
         } else if(estimateAmount > 100000) {
          map.put("councilResolutionDetailsRequired",true);
          map.put("contractCommitteeDetailsRequired",true);
         }
      } else if(cityGrade.equals("III") || cityGrade.equals("NP")) {
         if(estimateAmount <= 20000) {
          map.put("noFieldsRequired",true);
         } else if(estimateAmount > 20000 && estimateAmount <= 50000) {
          map.put("contractCommitteeDetailsRequired",true);
         } else if(estimateAmount > 50000) {
          map.put("councilResolutionDetailsRequired",true);
          map.put("contractCommitteeDetailsRequired",true);
         }
      } else if(cityGrade.equals("Selection")) {
        if(estimateAmount <= 500000) {
          map.put("noFieldsRequired",true);
         } else if(estimateAmount > 500000 && estimateAmount <= 1000000) {
          map.put("contractCommitteeDetailsRequired",true);
         } else if(estimateAmount > 1000000) {
          map.put("councilResolutionDetailsRequired",true);
          map.put("contractCommitteeDetailsRequired",true);
         }
      } else if(cityGrade.equals("Special")) {
        if(estimateAmount <= 200000) {
          map.put("noFieldsRequired",true);
         } else if(estimateAmount > 200000 && estimateAmount <= 400000) {
          map.put("contractCommitteeDetailsRequired",true);
         } else if(estimateAmount > 400000) {
          map.put("councilResolutionDetailsRequired",true);
          map.put("contractCommitteeDetailsRequired",true);
         }
      }
      return map;
   }
   result = getWorkFlowFields();',now(),'01-Jan-2100',0); 