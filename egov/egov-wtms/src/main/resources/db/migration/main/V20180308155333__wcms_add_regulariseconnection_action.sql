INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version,createdby, createddate, lastmodifiedby, lastmodifieddate,application) VALUES (nextval('SEQ_EG_ACTION'), 'ApplyforRegulariseWaterConnection', '/application/regulariseconnection/new', null,(select id from eg_module where name='WaterTaxTransactions'), 9, 'Apply for Regularise Connection', true,'wtms', 0, 1, now(), 1, now(),(select id from eg_module where name='Water Tax Management'));

INSERT INTO eg_roleaction (roleid,actionid) values ((select id from eg_role where name='SYSTEM'),
(select id from eg_action where name='ApplyforRegulariseWaterConnection'));

INSERT INTO eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Water Tax Approver'),
(select id from eg_action where name='ApplyforRegulariseWaterConnection'));

INSERT INTO eg_roleaction (roleid,actionid) values ((select id from eg_role where name='ULB Operator'),
(select id from eg_action where name='ApplyforRegulariseWaterConnection'));

INSERT INTO eg_roleaction (roleid,actionid) values ((select id from eg_role where name='CSC Operator'),
(select id from eg_action where name='ApplyforRegulariseWaterConnection'));


INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'WaterConnectionDetails', 'Created', NULL, NULL, NULL, 'REGLZNCONNECTION', 'NEW', 'Revenue Clerk approval pending', 'Revenue Clerk', 'Clerk Approved Pending', 'Forward', NULL, NULL, '2015-08-01', '2099-04-01');

update eg_wf_matrix set pendingactions ='Revenue Clerk approval pending' where objecttype='WaterConnectionDetails' and additionalrule='REGLZNCONNECTION' and currentstate='NEW';

INSERT INTO egwtr_application_process_time (id,applicationtype,category,processingtime,active,createddate,lastmodifieddate,createdby,lastmodifiedby,version)
SELECT  nextval('SEQ_EGWTR_APPLICATION_PROCESS_TIME') ,(select id from egwtr_application_type where code ='REGLZNCONNECTION')
 ,id,15,true,now(),now(),1,1,0
FROM egwtr_category where active =true;

