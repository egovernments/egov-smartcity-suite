-------------------- Actions ----------------------
INSERT into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values 
(NEXTVAL('SEQ_EG_ACTION'),'vacancyRemissionApproval','/vacancyremission/approval/', null,(select id from EG_MODULE where name = 'Existing property'),1,
'vacancyRemissionApproval','f','ptis',0,1,now(),1,now(),(select id from eg_module  where name = 'Property Tax'));
INSERT into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values 
(NEXTVAL('SEQ_EG_ACTION'),'vacancyRemissionApprovalUpdate','/vacancyremission/approval/update/', null,(select id from EG_MODULE where name = 'Existing property'),1,
'vacancyRemissionApprovalUpdate','f','ptis',0,1,now(),1,now(),(select id from eg_module  where name = 'Property Tax'));


-------------------- Role Action Mappings ----------------------
INSERT INTO eg_roleaction(actionid, roleid) values ((select id from eg_action where name = 'vacancyRemissionApproval'), (Select id from eg_role where name='Property Verifier'));
---------------------- Role action mapping for Workflow -----------------------
INSERT INTO eg_roleaction(actionid, roleid) values ((select id from eg_action where name = 'vacancyRemissionApprovalUpdate'), 
(Select id from eg_role where name='Property Verifier'));
INSERT INTO eg_roleaction(actionid, roleid) values ((select id from eg_action where name = 'vacancyRemissionApprovalUpdate'), 
(Select id from eg_role where name='Property Approver'));
INSERT INTO eg_roleaction(actionid, roleid) values ((select id from eg_action where name = 'vacancyRemissionApprovalUpdate'), 
(Select id from eg_role where name='ULB Operator'));


---------------------- WF Types -----------------------
INSERT INTO eg_wf_types (id, module, type, link, createdby, createddate, lastmodifiedby, lastmodifieddate, renderyn, groupyn, typefqn, displayname, version) VALUES (nextval('seq_eg_wf_types'), (SELECT id FROM eg_module WHERE name='Property Tax'), 'VacancyRemissionApproval', '/ptis/vacancyremission/approval/update/:ID', 1, now(), 1, now(), 'Y', 'N', 'org.egov.ptis.domain.entity.property.VacancyRemissionApproval', 'Vacancy Remission Approval', 0);


