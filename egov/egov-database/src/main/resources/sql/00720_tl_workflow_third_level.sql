alter table eg_wf_matrix alter column nextstatus type character varying(100);

update eg_wf_matrix set nextstate = 'Create License:Assistant health officer Approved', nextaction = 'Health officer Approval pending',
nextdesignation='Health officer', nextstatus = 'Assistant health officer Approved', validactions = 'Forward,Reject'
where objecttype = 'TradeLicense' and currentstate = 'Create License:Sanitary inspector Approved';

INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'TradeLicense', 'Create License:Assistant health officer Approved', NULL, NULL, 
'Health officer', NULL, 'Create License:END', 'END', NULL, NULL, 'Approve,Reject', NULL, NULL, '2015-04-01', '2099-04-01');

INSERT INTO egeis_assignment (id,fund,function,designation,functionary,department,position,grade,lastmodifiedby,lastmodifieddate,createddate,createdby,
fromdate,todate,version,employee,isprimary) VALUES(nextval('seq_egeis_assignment'),(SELECT id FROM fund WHERE name='Municipal Fund'),null,
(SELECT id FROM eg_designation WHERE name='Health officer'),null,(SELECT id FROM eg_department WHERE name='Health'),
(SELECT id FROM eg_position WHERE name='H-HEALTH OFFICER-1'),1,1,now(),now(),1,'01-Apr-2015','31-Mar-2020',1,
(SELECT id FROM egeis_employee WHERE code='E055'),true);

Insert into eg_userrole values((select id from eg_role  where name  = 'TLApprover'),(select id from eg_user where username ='mussavir'));
Insert into eg_userrole values((select id from eg_role  where name  = 'Employee'),(select id from eg_user where username ='mussavir'));

INSERT INTO eg_roleaction (actionid, roleid) values ((select id from eg_action where name = 'AjaxDesignationDropdown'), (select id from eg_role where name = 'TLApprover'));
INSERT INTO eg_roleaction (actionid, roleid) values ((select id from eg_action where name = 'AjaxApproverDropdown'), (select id from eg_role where name = 'TLApprover'));
