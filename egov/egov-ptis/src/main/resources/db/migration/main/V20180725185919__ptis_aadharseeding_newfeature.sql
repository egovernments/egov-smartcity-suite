--aadhar seeding tables
create sequence seq_egpt_aadharseeding;
CREATE TABLE egpt_aadharseeding
(
  id bigint NOT NULL,
  basicproperty bigint NOT NULL,
  status varchar(20),
  flag boolean default false,
  isaadharvalid boolean default false,
  state_id bigint,
  createdby bigint,
  createddate timestamp without time zone,
  lastmodifiedby bigint,
  lastmodifieddate timestamp without time zone,
  version bigint,
  CONSTRAINT egpt_aadharseeding_pkey PRIMARY KEY (id)
);

create sequence seq_egpt_aadharseeding_details;
CREATE TABLE egpt_aadharseeding_details
(
  id bigint NOT NULL,
  aadharseeding bigint NOT NULL,
  owner bigint,
  createdby bigint,
  createddate timestamp without time zone,
  lastmodifiedby bigint,
  lastmodifieddate timestamp without time zone,
  version bigint,
  aadharno varchar(12),
  CONSTRAINT egpt_aadharseeding_details_pkey PRIMARY KEY (id),
  CONSTRAINT "fk_aadharseeing_details" FOREIGN KEY (aadharseeding) REFERENCES egpt_aadharseeding(id)
);

--aadhar seeding specific roles
insert into eg_role VALUES (nextval('seq_eg_role'),'Aadhar Initiator','Survey Seeding Initiator',now(),1,1,now(),0,true);
insert into eg_role VALUES (nextval('seq_eg_role'),'Aadhar Approver','Survey Seeding Approver',now(),1,1,now(),0,true);


--aadhar seeding workflow matrix
INSERT INTO eg_wf_types(id, module, type, link, createdby, createddate, lastmodifiedby, lastmodifieddate, enabled, grouped, typefqn, displayname, version) VALUES (nextval('seq_eg_wf_types'), (SELECT id FROM eg_module WHERE name='Property Tax'), 'AadharSeeding', '/ptis/aadhardataupdateform/{assessmentNo}', 1, now(), 1, now(), 'N', 'N', 'org.egov.ptis.bean.aadharseeding.AadharSeeding', 'Aadhar Seeding', 0);

INSERT INTO eg_wf_matrix(id, department, objecttype, currentstate, currentstatus, pendingactions,currentdesignation, additionalrule,nextstate,nextaction, nextdesignation,nextstatus, validactions, fromqty, toqty, fromdate, todate, version,enablefields,forwardenabled, smsemailenabled, nextref, rejectenabled) VALUES (nextval('seq_eg_wf_matrix'), 'REVENUE', 'AadharSeeding', 'AadharSeeding:New', null, 'Update Pending','Bill Collector, Tax Collector', 'AADHAR SEEDING', 'AadharSeeding:Updated', 'Approval Pending', 'Commissioner','Approved', 'Update', null, null, now(), '2099-04-01', 0,null, null, null, null, null);

INSERT INTO eg_wf_matrix(id, department, objecttype, currentstate, currentstatus, pendingactions,currentdesignation, additionalrule,nextstate,nextaction, nextdesignation,nextstatus, validactions, fromqty, toqty, fromdate, todate, version,enablefields,forwardenabled, smsemailenabled, nextref, rejectenabled) VALUES (nextval('seq_eg_wf_matrix'), 'ADMINISTRATION','AadharSeeding', 'AadharSeeding:Updated', 'Updated', 'Approval Pending','Commissioner', 'AADHAR SEEDING','AadharSeeding:Approved', 'END', null,'Approved', 'Close', null, null, now(), '2099-04-01', 0,null, null, null, null, null);


--aadhar seeding actions
INSERT INTO eg_module (id, name, enabled, contextroot, parentmodule, displayname, ordernumber) VALUES (nextval('SEQ_EG_MODULE'), 'Aadhar Seeding', true, 'ptis', (select id from eg_module where name='Property Tax'), 'Aadhar Seeding', null);

insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,
lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'AadharSeedingSearch','/aadharseeding/search',null,(select id from eg_module where name='Aadhar Seeding'),1,'Aadhar Seeding Search',false,'ptis',0,1,now(),1,
now(),(select id from eg_module where name='Aadhar Seeding'));

insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,
lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'AadharSeedingResult','/aadharseeding/result',null,(select id from eg_module where name='Aadhar Seeding'),1,'Aadhar Seeding Result',false,'ptis',0,1,now(),1,
now(),(select id from eg_module where name='Aadhar Seeding'));

insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,
lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'AadharSeedingUpdateForm','/aadharseeding/aadhardataupdateform',null,(select id from eg_module where name='Aadhar Seeding'),1,'Aadhar Seeding Update Form',false,'ptis',0,1,now(),1,
now(),(select id from eg_module where name='Aadhar Seeding'));

insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,
lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'AadharSeedingApprovalForm','/aadharseeding/aadhardataapprovalform',null,(select id from eg_module where name='Aadhar Seeding'),1,'Approve Aadhar Seeding',false,'ptis',0,1,now(),1,
now(),(select id from eg_module where name='Aadhar Seeding'));

insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,
lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'PeopleHubData','/aadharseeding/peoplehubdata',null,(select id from eg_module where name='Aadhar Seeding'),1,'View PeopleHub Data',false,'ptis',0,1,now(),1,
now(),(select id from eg_module where name='Aadhar Seeding'));




