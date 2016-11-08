-----Employee position report roleactions-----
INSERT INTO eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values(nextval('SEQ_EG_ACTION'),'EmployeePositionData','/report/employeePositionReport',null,
(select id from eg_module where name='EIS Reports'),1,'Employee Position Report','true','eis',0,1,now(),1,now(),(select id from eg_module  where name = 'EIS'));


INSERT INTO eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values(nextval('SEQ_EG_ACTION'),'EmployeePositionList','/report/empPositionList',null,
(select id from eg_module where name='EIS Reports'),1,'Employee Position List','false','eis',0,1,now(),1,now(),(select id from eg_module  where name = 'EIS'));


insert into eg_roleaction values((select id from eg_role where name='Employee Admin'),(select id from eg_action where name='EmployeePositionData' and contextroot = 'eis'));
insert into eg_roleaction values((select id from eg_role where name = 'ERP Report Viewer'),(select id from eg_action where name ='EmployeePositionData' and contextroot = 'eis'));
insert into eg_roleaction values((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='EmployeePositionData' and contextroot = 'eis'));
insert into eg_roleaction values((select id from eg_role where name = 'EIS Report Viewer'),(select id from eg_action where name ='EmployeePositionData' and contextroot = 'eis'));

insert into eg_roleaction values((select id from eg_role where name='Employee Admin'),(select id from eg_action where name='EmployeePositionList' and contextroot = 'eis'));
insert into eg_roleaction values((select id from eg_role where name = 'ERP Report Viewer'),(select id from eg_action where name ='EmployeePositionList' and contextroot = 'eis'));
insert into eg_roleaction values((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='EmployeePositionList' and contextroot = 'eis'));
insert into eg_roleaction values((select id from eg_role where name = 'EIS Report Viewer'),(select id from eg_action where name ='EmployeePositionList' and contextroot = 'eis'));



---Employee Position Report feature mapping--

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Employee Position Report','Employee Position Report',(select id from eg_module  where name = 'EIS'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'EmployeePositionData') ,(select id FROM eg_feature WHERE name = 'Employee Assignment Report'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'EmployeePositionList') ,(select id FROM eg_feature WHERE name = 'Employee Assignment Report'));


INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Employee Position Report'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Employee Admin') ,(select id FROM eg_feature WHERE name = 'Employee Position Report'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'ERP Report Viewer') ,(select id FROM eg_feature WHERE name = 'Employee Position Report'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'EIS Report Viewer') ,(select id FROM eg_feature WHERE name = 'Employee Position Report'));