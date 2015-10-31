-------------------- Actions ----------------------
INSERT into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values 
(NEXTVAL('SEQ_EG_ACTION'),'vacancyRemission','/property/vacancyremission', null,(select id from EG_MODULE where name = 'Existing property'),1,
'vacancyRemission','f','ptis',0,1,now(),1,now(),(select id from eg_module  where name = 'Property Tax'));
INSERT into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values 
(NEXTVAL('SEQ_EG_ACTION'),'vacancyRemissionRejectionAck','/vacancyremission/rejectionacknowledgement', null,(select id from EG_MODULE where name = 'Existing property'),1,
'vacancyRemissionRejectionAck','f','ptis',0,1,now(),1,now(),(select id from eg_module  where name = 'Property Tax'));


-------------------- Role Action Mappings ----------------------
INSERT INTO eg_roleaction(actionid, roleid) values ((select id from eg_action where name = 'vacancyRemission'), (Select id from eg_role where name='ULB Operator'));
INSERT INTO eg_roleaction(actionid, roleid) values ((select id from eg_action where name = 'vacancyRemissionRejectionAck'), (Select id from eg_role where name='ULB Operator'));
---------------------- Role action mapping for Commissioner -----------------------
INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Property Verifier'),(select id from eg_action where name='AjaxDesignationsByDepartment'));
INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Property Approver'),(select id from eg_action where name='AjaxDesignationsByDepartment'));
INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Property Verifier'),(select id from eg_action where name='AjaxApproverByDesignationAndDepartment'));
INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Property Approver'),(select id from eg_action where name='AjaxApproverByDesignationAndDepartment'));


---------------------- WF Types -----------------------
INSERT INTO eg_wf_types (id, module, type, link, createdby, createddate, lastmodifiedby, lastmodifieddate, renderyn, groupyn, typefqn, displayname, version) VALUES (nextval('seq_eg_wf_types'), (SELECT id FROM eg_module WHERE name='Property Tax'), 'VacancyRemission', '/ptis/vacancyremission/update/:ID', 1, now(), 1, now(), 'Y', 'N', 'org.egov.ptis.domain.entity.property.VacancyRemission', 'Vacancy Remission', 0);


