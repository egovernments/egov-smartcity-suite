create table egwtr_regularise_connection_detail (
id bigint not null,
ulbcode character varying(4),
propertyidentifier character varying(20),
state_id bigint,
createdby bigint not null,
createddate timestamp without time zone not null,
lastmodifieddate timestamp without time zone not null,
lastmodifiedby bigint not null,
version numeric not null,
constraint pk_regularisedconn primary key (id),
constraint fk_regularisedconn foreign key (state_id) references eg_wf_states(id) match simple on update no action on delete no action
);

create sequence seq_egwtr_regularise_connection_detail;

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'RegularisedConnection', 'Created', NULL, NULL, 'Revenue Clerk', 'REGLZNCONNECTION', 'New', 'Revenue clerk approval pending', 'Senior Assistant,Junior Assistant', 'Clerk Approved Pending', 'Forward', NULL, NULL, '2015-08-01', '2099-04-01');

INSERT INTO eg_wf_types (id, module, type, link, createdby, createddate, lastmodifiedby, lastmodifieddate, enabled, grouped, typefqn, displayname, version) VALUES (nextval('seq_eg_wf_types'), 2, 'RegularisedConnection', '/wtms/application/regulariseconnection-form/:ID', 1, '2015-08-28 10:45:18.201078', 1, '2015-08-28 10:45:18.201078', true, false, 'org.egov.wtms.application.entity.RegularisedConnection', 'Water Tap Regularised Connection', 0);

Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'WaterTaxCreateRegularisedConnectionNewForm','/application/regularisedconnection-form',null,(select id from eg_module where name='WaterTaxTransactions'),2,'Regularise Water Tap Connection',false,'wtms',0,1,now(),1,now(),(select id from eg_module where name='Water Tax Management'));
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='SYSTEM'),(select id from eg_action where name='WaterTaxCreateRegularisedConnectionNewForm'));
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='CSC Operator'),(select id from eg_action where name='WaterTaxCreateRegularisedConnectionNewForm'));
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='ULB Operator'),(select id from eg_action where name='WaterTaxCreateRegularisedConnectionNewForm'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WaterTaxCreateRegularisedConnectionNewForm') ,(select id FROM eg_feature WHERE name = 'Update WaterTax Connection'));
