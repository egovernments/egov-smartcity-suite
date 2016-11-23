INSERT INTO eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values(nextval('SEQ_EG_ACTION'),'EmployeePositionsList','/report/positions',null,
(select id from eg_module where name='EIS Reports'),1,'Employee Positions List','false','eis',0,1,now(),1,now(),(select id from eg_module  where name = 'EIS'));


insert into eg_roleaction values((select id from eg_role where name='Employee Admin'),(select id from eg_action where name='EmployeePositionsList' and contextroot = 'eis'));
insert into eg_roleaction values((select id from eg_role where name = 'ERP Report Viewer'),(select id from eg_action where name ='EmployeePositionsList' and contextroot = 'eis'));
insert into eg_roleaction values((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='EmployeePositionsList' and contextroot = 'eis'));
insert into eg_roleaction values((select id from eg_role where name = 'EIS Report Viewer'),(select id from eg_action where name ='EmployeePositionsList' and contextroot = 'eis'));

update eg_feature_action set feature=(select id FROM eg_feature WHERE name = 'Employee Position Report') where action in (select id FROM eg_action  WHERE name in ( 'EmployeePositionData','EmployeePositionList'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'EmployeePositionsList') ,(select id FROM eg_feature WHERE name = 'Employee Position Report'));