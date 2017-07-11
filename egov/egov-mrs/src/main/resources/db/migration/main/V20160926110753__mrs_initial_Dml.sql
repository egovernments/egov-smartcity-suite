-----------------START-------------------
INSERT INTO eg_module (id, name, enabled, contextroot, parentmodule, displayname, ordernumber) VALUES (nextval('SEQ_EG_MODULE'), 'Marriage Registration', true, 'mrs', NULL, 'Marriage Registration', 30);
INSERT INTO eg_module (id, name, enabled, contextroot, parentmodule, displayname, ordernumber) VALUES (nextval('SEQ_EG_MODULE'), 'MR-Masters', true, NULL, (select id from eg_module where name='Marriage Registration'), 'Masters', NULL);
INSERT INTO eg_module (id, name, enabled, contextroot, parentmodule, displayname, ordernumber) VALUES (nextval('SEQ_EG_MODULE'), 'MR-Transactions', true, NULL, (select id from eg_module where name='Marriage Registration'), 'Transactions', NULL);
INSERT INTO eg_module (id, name, enabled, contextroot, parentmodule, displayname, ordernumber) VALUES (nextval('SEQ_EG_MODULE'), 'MR-Reports', true, NULL, (select id from eg_module where name='Marriage Registration'), 'Reports', NULL);
------------------END---------------------

-----------------START-------------------
INSERT INTO eg_demand_reason_master (id, reasonmaster, "category", isdebit, module, code, "order", create_date, modified_date, isdemand) VALUES(nextval('seq_eg_demand_reason_master'), 'Registration', (select id from eg_reason_category where code='Fee'), 'N', (select id from eg_module where name='Marriage Registration'), 'REGISTRATION', 1, current_timestamp, current_timestamp,'true');
INSERT INTO eg_demand_reason_master (id, reasonmaster, "category", isdebit, module, code, "order", create_date, modified_date, isdemand) VALUES(nextval('seq_eg_demand_reason_master'), 'Re-Issue', (select id from eg_reason_category where code='Fee'), 'N', (select id from eg_module where name='Marriage Registration'), 'REISSUE', 2, current_timestamp, current_timestamp,'true');
------------------END---------------------


-----------------START-------------------
INSERT INTO eg_wf_types (id,module,type,link,createdby,createddate,lastmodifiedby,lastmodifieddate,enabled,grouped,typefqn,displayname,version) VALUES (nextval('seq_eg_wf_types'),(SELECT id FROM eg_module WHERE name='Marriage Registration'),'Registration','/mrs/registration/update/:ID',1,now(),1,now(), 'Y', 'N','org.egov.mrs.domain.entity.Registration', 'Registration',0);------------------END---------------------

-----------------START-------------------
INSERT INTO eg_installment_master (id, installment_num, installment_year, start_date, end_date, id_module, lastupdatedtimestamp, description, installment_type) VALUES (nextval('seq_eg_installment_master'), 1, '2015-10-01 00:00:00', '2015-10-01 00:00:00', '2016-03-31 23:59:59', (select id from eg_module where name='Marriage Registration'), current_timestamp, '2015-2016-2', NULL);
INSERT INTO eg_installment_master (id, installment_num, installment_year, start_date, end_date, id_module, lastupdatedtimestamp, description, installment_type) VALUES (nextval('seq_eg_installment_master'), 1, '2016-04-01 00:00:00', '2016-04-01 00:00:00', '2016-09-30 23:59:59', (select id from eg_module where name='Marriage Registration'), current_timestamp, '2016-2017-1', NULL);
INSERT INTO eg_installment_master (id, installment_num, installment_year, start_date, end_date, id_module, lastupdatedtimestamp, description, installment_type) VALUES (nextval('seq_eg_installment_master'), 1, '2016-10-01 00:00:00', '2016-10-01 00:00:00', '2017-03-31 23:59:59', (select id from eg_module where name='Marriage Registration'), current_timestamp, '2016-2017-2', NULL);
INSERT INTO eg_installment_master (id, installment_num, installment_year, start_date, end_date, id_module, lastupdatedtimestamp, description, installment_type) VALUES (nextval('seq_eg_installment_master'), 1, '2017-04-01 00:00:00', '2017-04-01 00:00:00', '2017-09-30 23:59:59', (select id from eg_module where name='Marriage Registration'), current_timestamp, '2017-2018-1', NULL);
INSERT INTO eg_installment_master (id, installment_num, installment_year, start_date, end_date, id_module, lastupdatedtimestamp, description, installment_type) VALUES (nextval('seq_eg_installment_master'), 1, '2017-10-01 00:00:00', '2017-10-01 00:00:00', '2018-03-31 23:59:59', (select id from eg_module where name='Marriage Registration'), current_timestamp, '2017-2018-2', NULL);
INSERT INTO eg_installment_master (id, installment_num, installment_year, start_date, end_date, id_module, lastupdatedtimestamp, description, installment_type) VALUES (nextval('seq_eg_installment_master'), 1, '2018-04-01 00:00:00', '2018-04-01 00:00:00', '2018-09-30 23:59:59', (select id from eg_module where name='Marriage Registration'), current_timestamp, '2018-2019-1', NULL);
INSERT INTO eg_installment_master (id, installment_num, installment_year, start_date, end_date, id_module, lastupdatedtimestamp, description, installment_type) VALUES (nextval('seq_eg_installment_master'), 1, '2018-10-01 00:00:00', '2018-10-01 00:00:00', '2019-03-31 23:59:59', (select id from eg_module where name='Marriage Registration'), current_timestamp, '2018-2019-2', NULL);
INSERT INTO eg_installment_master (id, installment_num, installment_year, start_date, end_date, id_module, lastupdatedtimestamp, description, installment_type) VALUES (nextval('seq_eg_installment_master'), 1, '2019-04-01 00:00:00', '2019-04-01 00:00:00', '2019-09-30 23:59:59', (select id from eg_module where name='Marriage Registration'), current_timestamp, '2019-2020-1', NULL);
INSERT INTO eg_installment_master (id, installment_num, installment_year, start_date, end_date, id_module, lastupdatedtimestamp, description, installment_type) VALUES (nextval('seq_eg_installment_master'), 1, '2019-10-01 00:00:00', '2019-10-01 00:00:00', '2020-03-31 23:59:59', (select id from eg_module where name='Marriage Registration'), current_timestamp, '2019-2020-2', NULL);
------------------END---------------------


-----------------START-------------------
Insert into eg_demand_reason (id, id_demand_reason_master, id_installment, percentage_basis, id_base_reason, create_date, modified_date, glcodeid) 
select nextval('seq_eg_demand_reason'), (select id from eg_demand_reason_master where reasonmaster='Registration' and module=(select id from eg_module where name='Marriage Registration')), inst.id, null, null, current_timestamp, current_timestamp, (select id from chartofaccounts where name='Fees for Certificate or Extract-Copy of Plan/Certificate') from eg_installment_master inst where inst.id_module=(select id from eg_module where name='Marriage Registration');
Insert into eg_demand_reason (id, id_demand_reason_master, id_installment, percentage_basis, id_base_reason, create_date, modified_date, glcodeid) 
select nextval('seq_eg_demand_reason'), (select id from eg_demand_reason_master where reasonmaster='Re-Issue' and module=(select id from eg_module where name='Marriage Registration')), inst.id, null, null, current_timestamp, current_timestamp, (select id from chartofaccounts where name='Fees for Certificate or Extract-Copy of Plan/Certificate') from eg_installment_master inst where  inst.id_module=(select id from eg_module where name='Marriage Registration'); 
--and start_date >= (select start_date from eg_installment_master where id_module=(select id from eg_module where name='Marriage Registration') and now() between start_date and end_date);
------------------END---------------------


-----------------START-------------------
INSERT INTO egmrs_fee(id, criteria, fees, "version",fromdays,todays, createdby, createddate, lastmodifiedby, lastmodifieddate) VALUES(nextval('seq_egmrs_fee'), '0 to 90 days', 50, 1,0,90,1, now(), 1, now());
INSERT INTO egmrs_fee(id, criteria, fees, "version",fromdays,todays, createdby, createddate, lastmodifiedby, lastmodifieddate) VALUES(nextval('seq_egmrs_fee'), '91 to 365 days', 150, 1,91,365, 1, now(), 1, now());
INSERT INTO egmrs_fee(id, criteria, fees, "version",fromdays, createdby, createddate, lastmodifiedby, lastmodifieddate) VALUES(nextval('seq_egmrs_fee'), '366 days and above', 250, 1,366, 1, now(), 1, now());
INSERT INTO egmrs_fee(id, criteria, fees, "version", createdby, createddate, lastmodifiedby, lastmodifieddate) VALUES(nextval('seq_egmrs_fee'), 'Re-Issue Fee', 50, 1, 1, now(), 1, now());
------------------END---------------------

-----------------START-------------------
INSERT INTO egmrs_act(id, "name", description, "version", createdby, createddate, lastmodifiedby, lastmodifieddate) VALUES(nextval('seq_egmrs_act'), 'Christian Marriage Acts', 'Christian Marriage Acts', 1, 1, now(), 1, now());
INSERT INTO egmrs_act(id, "name", description, "version", createdby, createddate, lastmodifiedby, lastmodifieddate) VALUES(nextval('seq_egmrs_act'), 'Hindu Marriage Acts', 'Hindu Marriage Acts', 1, 1, now(), 1, now());
INSERT INTO egmrs_act(id, "name", description, "version", createdby, createddate, lastmodifiedby, lastmodifieddate) VALUES(nextval('seq_egmrs_act'), 'Muslim Marriage Acts', 'Muslim Marriage Acts', 1, 1, now(), 1, now());
INSERT INTO egmrs_act(id, "name", description, "version", createdby, createddate, lastmodifiedby, lastmodifieddate) VALUES(nextval('seq_egmrs_act'), 'Parsi Marriage Acts', 'Parsi Marriage Acts', 1, 1, now(), 1, now());
INSERT INTO egmrs_act(id, "name", description, "version", createdby, createddate, lastmodifiedby, lastmodifieddate) VALUES(nextval('seq_egmrs_act'), 'Special Marriage Acts', 'Apecial Marriage Acts', 1, 1, now(), 1, now());
INSERT INTO egmrs_act(id, "name", description, "version", createdby, createddate, lastmodifiedby, lastmodifieddate) VALUES(nextval('seq_egmrs_act'), 'Others', 'Others', 1, 1, now(), 1, now());
------------------END---------------------

-----------------START-------------------
INSERT INTO egmrs_religion(id, "name", description, "version", createdby, createddate, lastmodifiedby, lastmodifieddate) VALUES(nextval('seq_egmrs_religion'), 'Christian', 'Christian', 1, 1, now(), 1, now());
INSERT INTO egmrs_religion(id, "name", description, "version", createdby, createddate, lastmodifiedby, lastmodifieddate) VALUES(nextval('seq_egmrs_religion'), 'Hindu', 'Hindu', 1, 1, now(), 1, now());
INSERT INTO egmrs_religion(id, "name", description, "version", createdby, createddate, lastmodifiedby, lastmodifieddate) VALUES(nextval('seq_egmrs_religion'), 'Muslim', 'Muslim', 1, 1, now(), 1, now());
INSERT INTO egmrs_religion(id, "name", description, "version", createdby, createddate, lastmodifiedby, lastmodifieddate) VALUES(nextval('seq_egmrs_religion'), 'Parsi', 'Parsi', 1, 1, now(), 1, now());
INSERT INTO egmrs_religion(id, "name", description, "version", createdby, createddate, lastmodifiedby, lastmodifieddate) VALUES(nextval('seq_egmrs_religion'), 'Others', 'Others', 1, 1, now(), 1, now());
------------------END---------------------

-----------------START-------------------

Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) 
values (nextval('SEQ_EG_ACTION'),'MRIndexPage','/index.jsp',null,(select id from eg_module where name='Marriage Registration'), 1, 'Marriage Registration Index', false, 'mrs', 0, 1, now(),1, now(),(select id from eg_module where name='Marriage Registration'));

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Super User'), (SELECT id FROM eg_action WHERE name = 'MRIndexPage'));

		---- 0 ----
		
INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, "version", createdby, createddate, lastmodifiedby, lastmodifieddate, application) 
VALUES (nextval('seq_eg_action'), 'CreateReligion', '/masters/religion/create', NULL, (SELECT id FROM eg_module WHERE name = 'MR-Masters'), 1, 'Create Religion', true, 'mrs', 0, 1, now(), 1, now(), (select id from eg_module where name='Marriage Registration'));

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Super User'), (SELECT id FROM eg_action WHERE name = 'CreateReligion'));
		---- 0 ----

INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, "version", createdby, createddate, lastmodifiedby, lastmodifieddate, application) 
VALUES (nextval('seq_eg_action'), 'CreateRegistration', '/registration/register', NULL, (SELECT id FROM eg_module WHERE name = 'MR-Transactions'), 2, 'Create Marriage Registration', true, 'mrs', 0, 1, now(), 1, now(), (select id from eg_module where name='Marriage Registration'));

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Super User'), (SELECT id FROM eg_action WHERE name = 'CreateRegistration'));

		---- 0 ----

INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, "version", createdby, createddate, lastmodifiedby, lastmodifieddate, application) 
VALUES (nextval('seq_eg_action'), 'SearchRegistration', '/registration/search', NULL, (SELECT id FROM eg_module WHERE name = 'MR-Transactions'), 3, 'Search Marriage Registration', true, 'mrs', 0, 1, now(), 1, now(), (select id from eg_module where name='Marriage Registration'));

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Super User'), (SELECT id FROM eg_action WHERE name = 'SearchRegistration'));

		---- 0 ----

INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, "version", createdby, createddate, lastmodifiedby, lastmodifieddate, application) 
VALUES (nextval('seq_eg_action'), 'MarriageFeeBill', '/collection/bill', NULL, (SELECT id FROM eg_module WHERE name = 'MR-Transactions'), 4, 'Marriage Fee Bill', false, 'mrs', 0, 1, now(), 1, now(), (select id from eg_module where name='Marriage Registration'));

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Super User'), (SELECT id FROM eg_action WHERE name = 'MarriageFeeBill'));

		---- 0 ----

INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, "version", createdby, createddate, lastmodifiedby, lastmodifieddate, application) 
VALUES (nextval('seq_eg_action'), 'RegistrationDataEntry', '/registration/update', NULL, (SELECT id FROM eg_module WHERE name = 'MR-Transactions'), 5, 'Registration DataEntry', false, 'mrs', 0, 1, now(), 1, now(), (select id from eg_module where name='Marriage Registration'));

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Super User'), (SELECT id FROM eg_action WHERE name = 'RegistrationDataEntry'));

		---- 0 ----

INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, "version", createdby, createddate, lastmodifiedby, lastmodifieddate, application) 
VALUES (nextval('seq_eg_action'), 'RegistrationStatusReport', '/report/registrationstatus', NULL, (SELECT id FROM eg_module WHERE name = 'MR-Reports'), 6, 'Registration Status', true, 'mrs', 0, 1, now(), 1, now(), (select id from eg_module where name='Marriage Registration'));

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Super User'), (SELECT id FROM eg_action WHERE name = 'RegistrationStatusReport'));

		---- 0 ----

INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, "version", createdby, createddate, lastmodifiedby, lastmodifieddate, application) 
VALUES (nextval('seq_eg_action'), 'CreateReIssue', '/reissue/create', NULL, (SELECT id FROM eg_module WHERE name = 'MR-Transactions'), 7, 'Marriage Registration Re-Issue', true, 'mrs', 0, 1, now(), 1, now(), (select id from eg_module where name='Marriage Registration'));

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Super User'), (SELECT id FROM eg_action WHERE name = 'CreateReIssue'));

		---- 0 ----

INSERT INTO egcl_servicecategory(id, name, code, isactive, version, createdby, createddate, lastmodifiedby, lastmodifieddate)
VALUES(nextval('seq_egcl_servicecategory'), 'Marriage Registration', 'MR', true, 0, 1, current_timestamp, 1, current_timestamp);

Insert into egcl_servicedetails (id,name,serviceurl,isenabled,callbackurl,servicetype,code,fund,fundsource,functionary,vouchercreation,scheme,subscheme,servicecategory,isvoucherapproved,vouchercutoffdate,created_by,created_date,modified_by,modified_date,ordernumber) 
values (nextval('seq_egcl_servicedetails'), 'MR Fee', '/../mrs/collection/bill', true, '/receipts/receipt-create.action', 'B', 'MRF', null, null, null, false, null, null, (select id from egcl_servicecategory where code='MR'), false, now(), 1, now(), 1, now(),null);


INSERT INTO egcl_servicecategory(id, name, code, isactive, version, createdby, createddate, lastmodifiedby, lastmodifieddate)
VALUES(nextval('seq_egcl_servicecategory'), 'Marriage Registration ReIssue', 'MRR', true, 0, 1, current_timestamp, 1, current_timestamp);

Insert into egcl_servicedetails (id,name,serviceurl,isenabled,callbackurl,servicetype,code,fund,fundsource,functionary,vouchercreation,scheme,subscheme,servicecategory,isvoucherapproved,vouchercutoffdate,created_by,created_date,modified_by,modified_date,ordernumber) 
values (nextval('seq_egcl_servicedetails'), 'MRR Fee', '/../mrs/collection/bill', true, '/receipts/receipt-create.action', 'B', 'MRRF', null, null, null, false, null, null, (select id from egcl_servicecategory where code='MRR'), false, now(), 1, now(), 1, now(),null);

--delete from egcl_servicedetails where name = 'MRR Fee';
--delete from egcl_servicecategory wheRe code = 'MRR'



INSERT INTO egmrs_document(ID, NAME,type, CODE, INDIVIDUAL, VERSION, CREATEDBY, CREATEDDATE, LASTMODIFIEDBY, LASTMODIFIEDDATE)
VALUES(nextval('seq_egmrs_document'), 'Memorandum of Marriage','REGISTRATION', 'MoM', false, 1, 1, now(), 1,  now());

INSERT INTO egmrs_document(ID, NAME, type,CODE, INDIVIDUAL, VERSION, CREATEDBY, CREATEDDATE, LASTMODIFIEDBY, LASTMODIFIEDDATE)
VALUES(nextval('seq_egmrs_document'), '&#x20B9; 100/- court fee stamps ( under provision court fee act 1859) affixed', 'REGISTRATION','CF_STAMP', false, 1, 1, now(), 1,  now());

INSERT INTO egmrs_document(ID, NAME, type,CODE, INDIVIDUAL, VERSION, CREATEDBY, CREATEDDATE, LASTMODIFIEDBY, LASTMODIFIEDDATE)
VALUES(nextval('seq_egmrs_document'), 'Affidavit (If necessary under sec. 5(3) In case of doubtful case)','REGISTRATION', 'AFFIDAVIT', false, 1, 1, now(), 1,  now());

INSERT INTO egmrs_document(ID, NAME,type, CODE, INDIVIDUAL, VERSION, CREATEDBY, CREATEDDATE, LASTMODIFIEDBY, LASTMODIFIEDDATE)
VALUES(nextval('seq_egmrs_document'), 'Marriage Invitation Card','REGISTRATION', 'MIC', false, 1, 1, now(), 1,  now());

INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, "version", createdby, createddate, lastmodifiedby, lastmodifieddate, application) 
VALUES (nextval('seq_eg_action'), 'View Marriage Registration', '/registration/view', NULL, (SELECT id FROM eg_module WHERE name = 'MR-Transactions'), 3, 'View Marriage Registration', false, 'mrs', 0, 1, now(), 1, now(), (select id from eg_module where name='Marriage Registration'));

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Super User'), (SELECT id FROM eg_action WHERE name = 'View Marriage Registration'));
