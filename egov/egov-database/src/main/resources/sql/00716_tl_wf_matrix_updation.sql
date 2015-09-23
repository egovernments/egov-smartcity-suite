INSERT INTO EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values 
(NEXTVAL('SEQ_EG_ACTION'),'NewTradeLicense-approve','/newtradelicense/newTradeLicense-approve.action',null,(select id from EG_MODULE where name = 'Trade License'),null,null,'f','tl',0,1,now(),1,now(),(select id from eg_module  where name = 'Trade License'));

INSERT INTO eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'TLCreator'),(select id from eg_action where name = 'ajax-populateDivisions'));
INSERT INTO eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'TLCreator'),(select id from eg_action where name = 'newTradeLicense-create'));

INSERT INTO eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'TLApprover'),(select id from eg_action where name = 'View Trade License Show for Approval'));
INSERT INTO eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'TLCreator'),(select id from eg_action where name = 'View Trade License Show for Approval'));
INSERT INTO eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'TLCreator'),(select id from eg_action where name = 'New Trade License Before Edit'));
INSERT INTO eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'TLCreator'),(select id from eg_action where name = 'editTradeLicense-edit'));

INSERT INTO eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'TLApprover'),(select id from eg_action where name = 'NewTradeLicense-approve'));

INSERT INTO eg_wf_types (id, module, type, link, createdby, createddate, lastmodifiedby, lastmodifieddate, renderyn, groupyn, typefqn, displayname, version)
values (nextval('seq_eg_wf_types'), (select id from eg_module where name = 'Trade License'), 'TradeLicense', '/tl/viewtradelicense/viewTradeLicense-showForApproval.action?model.id=:ID',
1, now(), 1, now(), 'Y', 'N', 'org.egov.tl.domain.entity.TradeLicense', 'Trade License', 0);

delete from eg_wf_matrix where objecttype = 'TradeLicense' and currentstate  = 'Assistant health officer Approved';

INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'TradeLicense', 'Sanitary inspector Approved', NULL, NULL, 'Assistant health officer', NULL, 'END', 'END', NULL, NULL, 'Approve,Reject', NULL, NULL, '2015-04-01', '2099-04-01');

update eg_wf_matrix set currentstate = 'Create License:' || currentstate, nextstate = 'Create License:' || nextstate where objecttype = 'TradeLicense';

