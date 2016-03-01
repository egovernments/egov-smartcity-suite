Insert into EG_ACTION ("id","name","url","queryparams","parentmodule","ordernumber","displayname","enabled","contextroot","version","createdby","createddate","lastmodifiedby","lastmodifieddate","application") values (nextval('seq_eg_action'),'CreateChallan','/receipts/challan-newform.action',null,(select ID from eg_module where NAME ='Receipt Services'),1,'Create Challan','1','collection',0,1,current_timestamp,1,current_timestamp,(select id from eg_module where name='Collection' and parentmodule is null));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'ULB Operator') ,(select id FROM eg_action  WHERE name = 'CreateChallan' and contextroot='collection'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'CSC Operator') ,(select id FROM eg_action  WHERE name = 'CreateChallan' and contextroot='collection'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Super User') ,(select id FROM eg_action  WHERE name = 'CreateChallan' and contextroot='collection'));

Insert into EG_ACTION ("id","name","url","queryparams","parentmodule","ordernumber","displayname","enabled","contextroot","version","createdby","createddate","lastmodifiedby","lastmodifieddate","application") values (nextval('seq_eg_action'),'AjaxChallanApproverDesignation','/receipts/ajaxChallanApproval-approverDesignationList.action',null,(select ID from eg_module where NAME ='Receipt Services'),1,'AjaxChallanApproverDesignation','0','collection',0,1,current_timestamp,1,current_timestamp,(select id from eg_module where name='Collection' and parentmodule is null));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'ULB Operator') ,(select id FROM eg_action  WHERE name = 'AjaxChallanApproverDesignation' and contextroot='collection'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'CSC Operator') ,(select id FROM eg_action  WHERE name = 'AjaxChallanApproverDesignation' and contextroot='collection'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Super User') ,(select id FROM eg_action  WHERE name = 'AjaxChallanApproverDesignation' and contextroot='collection'));

Insert into EG_ACTION ("id","name","url","queryparams","parentmodule","ordernumber","displayname","enabled","contextroot","version","createdby","createddate","lastmodifiedby","lastmodifieddate","application") values (nextval('seq_eg_action'),'AjaxChallanApproverPosition','/receipts/ajaxChallanApproval-positionUserList.action',null,(select ID from eg_module where NAME ='Receipt Services'),1,'AjaxChallanApproverPosition','0','collection',0,1,current_timestamp,1,current_timestamp,(select id from eg_module where name='Collection' and parentmodule is null));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'ULB Operator') ,(select id FROM eg_action  WHERE name = 'AjaxChallanApproverPosition' and contextroot='collection'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'CSC Operator') ,(select id FROM eg_action  WHERE name = 'AjaxChallanApproverPosition' and contextroot='collection'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Super User') ,(select id FROM eg_action  WHERE name = 'AjaxChallanApproverPosition' and contextroot='collection'));


Insert into EG_ACTION ("id","name","url","queryparams","parentmodule","ordernumber","displayname","enabled","contextroot","version","createdby","createddate","lastmodifiedby","lastmodifieddate","application") values (nextval('seq_eg_action'),'SaveChallan','/receipts/challan-save.action',null,(select ID from eg_module where NAME ='Receipt Services'),1,'Save Challan','0','collection',0,1,current_timestamp,1,current_timestamp,(select id from eg_module where name='Collection' and parentmodule is null));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'ULB Operator') ,(select id FROM eg_action  WHERE name = 'SaveChallan' and contextroot='collection'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'CSC Operator') ,(select id FROM eg_action  WHERE name = 'SaveChallan' and contextroot='collection'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Super User') ,(select id FROM eg_action  WHERE name = 'SaveChallan' and contextroot='collection'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Remitter') ,(select id FROM eg_action  WHERE name = 'SaveChallan' and contextroot='collection'));

Insert into EG_ACTION ("id","name","url","queryparams","parentmodule","ordernumber","displayname","enabled","contextroot","version","createdby","createddate","lastmodifiedby","lastmodifieddate","application") values (nextval('seq_eg_action'),'PrintChallan','/receipts/challan-printChallan.action',null,(select ID from eg_module where NAME ='Receipt Services'),1,'Print Challan','0','collection',0,1,current_timestamp,1,current_timestamp,(select id from eg_module where name='Collection' and parentmodule is null));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'ULB Operator') ,(select id FROM eg_action  WHERE name = 'PrintChallan' and contextroot='collection'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'CSC Operator') ,(select id FROM eg_action  WHERE name = 'PrintChallan' and contextroot='collection'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Super User') ,(select id FROM eg_action  WHERE name = 'PrintChallan' and contextroot='collection'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Remitter') ,(select id FROM eg_action  WHERE name = 'PrintChallan' and contextroot='collection'));


Insert into EG_ACTION ("id","name","url","queryparams","parentmodule","ordernumber","displayname","enabled","contextroot","version","createdby","createddate","lastmodifiedby","lastmodifieddate","application") values (nextval('seq_eg_action'),'ViewChallan','/receipts/challan-viewChallan.action',null,(select ID from eg_module where NAME ='Receipt Services'),1,'View Challan','0','collection',0,1,current_timestamp,1,current_timestamp,(select id from eg_module where name='Collection' and parentmodule is null));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'ULB Operator') ,(select id FROM eg_action  WHERE name = 'ViewChallan' and contextroot='collection'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'CSC Operator') ,(select id FROM eg_action  WHERE name = 'ViewChallan' and contextroot='collection'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Super User') ,(select id FROM eg_action  WHERE name = 'ViewChallan' and contextroot='collection'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Remitter') ,(select id FROM eg_action  WHERE name = 'ViewChallan' and contextroot='collection'));


Insert into eg_wf_types (id,module,type,link,createdby,createddate,lastmodifiedby,lastmodifieddate,renderyn,groupyn,typefqn,displayname,version) values (nextval('seq_eg_wf_types'),(select id from eg_module where name='Collection'),'Challan','/collection/receipts/challan-viewChallan.action?sourcePage=inbox&challanId=:ID',1,now(),1,now(), 'Y', 'N', 'org.egov.collection.entity.Challan', 'Collections Challan', 0 );

Insert into EG_WF_ACTIONS (ID,TYPE,NAME,DESCRIPTION,CREATED_BY,CREATED_DATE,MODIFIED_BY,MODIFIED_DATE) values (nextval('EG_WF_ACTIONS_SEQ'),'Challan','CHALLAN_CREATE','Create Challan',1,now(),1,now());
Insert into EG_WF_ACTIONS (ID,TYPE,NAME,DESCRIPTION,CREATED_BY,CREATED_DATE,MODIFIED_BY,MODIFIED_DATE) values (nextval('EG_WF_ACTIONS_SEQ'),'Challan','CHALLAN_APPROVE','Approve Challan',1,now(),1,now());
Insert into EG_WF_ACTIONS (ID,TYPE,NAME,DESCRIPTION,CREATED_BY,CREATED_DATE,MODIFIED_BY,MODIFIED_DATE) values (nextval('EG_WF_ACTIONS_SEQ'),'Challan','CHALLAN_REJECT','Reject Challan',1,now(),1,now());
Insert into EG_WF_ACTIONS (ID,TYPE,NAME,DESCRIPTION,CREATED_BY,CREATED_DATE,MODIFIED_BY,MODIFIED_DATE) values (nextval('EG_WF_ACTIONS_SEQ'),'Challan','CHALLAN_NEW','Create Challan',1,now(),1,now());
Insert into EG_WF_ACTIONS (ID,TYPE,NAME,DESCRIPTION,CREATED_BY,CREATED_DATE,MODIFIED_BY,MODIFIED_DATE) values (nextval('EG_WF_ACTIONS_SEQ'),'Challan','CHALLAN_MODIFY','Modify Challan',1,now(),1,now());
Insert into EG_WF_ACTIONS (ID,TYPE,NAME,DESCRIPTION,CREATED_BY,CREATED_DATE,MODIFIED_BY,MODIFIED_DATE) values (nextval('EG_WF_ACTIONS_SEQ'),'Challan','CHALLAN_CANCEL','Cancel Challan',1,now(),1,now());
Insert into EG_WF_ACTIONS (ID,TYPE,NAME,DESCRIPTION,CREATED_BY,CREATED_DATE,MODIFIED_BY,MODIFIED_DATE) values (nextval('EG_WF_ACTIONS_SEQ'),'Challan','CHALLAN_CHECK','Check Challan',1,now(),1,now());
Insert into EG_WF_ACTIONS (ID,TYPE,NAME,DESCRIPTION,CREATED_BY,CREATED_DATE,MODIFIED_BY,MODIFIED_DATE) values (nextval('EG_WF_ACTIONS_SEQ'),'Challan','CHALLAN_VALIDATE','Validate Challan',1,now(),1,now());


INSERT INTO eg_script (id, name, type, createdby, createddate, lastmodifiedby, lastmodifieddate, script, startdate, enddate, version) 
VALUES (nextval('seq_eg_script'), 'Challan.workflow.validactions', 'python',1, now(), 1, now(), 'transitions={''DEFAULT'':[''CHALLAN_NEW''],''CREATED'':[''CHALLAN_VALIDATE'',''CHALLAN_REJECT''],''CANCELLED'':[''''],''REJECTED'':[''CHALLAN_MODIFY'',''CHALLAN_CANCEL''],''VALIDATED'':['''']}  
state=''DEFAULT''  
if wfItem.getCurrentState():  
    state=wfItem.getCurrentState().getValue()  
result=[]  
if state in transitions: result = transitions[state]', '1900-01-01 00:00:00', '2100-01-01 00:00:00', 0);


INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, VERSION, MODULE ) VALUES (nextval('SEQ_EG_APPCONFIG'), 'CHALLANVALIDUPTO','No of days till which a challan is valid',0, (select id from eg_module where name='Collection')); 
INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'),(SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='CHALLANVALIDUPTO'),current_date, '30',0);


Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (nextval('SEQ_EGW_STATUS'),'Challan','Created',to_date('06-04-10','DD-MM-RR'),'CREATED',1);
Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (nextval('SEQ_EGW_STATUS'),'Challan','Checked',to_date('06-04-10','DD-MM-RR'),'CHECKED',2);
Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (nextval('SEQ_EGW_STATUS'),'Challan','Approved',to_date('06-04-10','DD-MM-RR'),'APPROVED',3);
Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (nextval('SEQ_EGW_STATUS'),'Challan','Cancelled',to_date('06-04-10','DD-MM-RR'),'CANCELLED',4);
Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (nextval('SEQ_EGW_STATUS'),'Challan','Rejected',to_date('06-04-10','DD-MM-RR'),'REJECTED',5);
Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (nextval('SEQ_EGW_STATUS'),'Challan','Validated',to_date('07-04-10','DD-MM-RR'),'VALIDATED',1);



