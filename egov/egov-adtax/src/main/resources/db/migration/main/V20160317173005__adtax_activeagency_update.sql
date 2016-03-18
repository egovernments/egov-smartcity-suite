INSERT INTO EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) 
VALUES (nextval('SEQ_EG_ACTION'),'ActiveAgencyAjaxDropdown','/agency/active-agencies',null,(select id from eg_module where name='ADTAX-COMMON'),2,'ActiveAgencyAjaxDropdown',false,'adtax',0,1,to_timestamp('2016-02-17 05:17:27.3235','null'),1,to_timestamp('2016-02-17 05:17:27.32357','null'),(select id from eg_module where name='Advertisement Tax'));


INSERT INTO  EG_ROLEACTION (actionid, roleid) select (select id from eg_action where name = 'ActiveAgencyAjaxDropdown'), id from eg_role where name in ('Super User');
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where (name) LIKE 'CSC Operator') ,(select id FROM eg_action  WHERE name = 'ActiveAgencyAjaxDropdown'));
insert into eg_roleaction values ((select id from eg_role where name='Advertisement Tax Creator'),(select id from eg_action where name='ActiveAgencyAjaxDropdown'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Advertisement Tax Approver') ,(select id FROM eg_action  WHERE name = 'ActiveAgencyAjaxDropdown'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Advertisement Tax Admin') ,(select id FROM eg_action  WHERE name = 'ActiveAgencyAjaxDropdown'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Collection Operator') ,(select id FROM eg_action  WHERE name = 'ActiveAgencyAjaxDropdown'));
