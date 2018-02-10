insert into eg_module(id,name,enabled,contextroot,parentmodule,displayname,ordernumber) values (nextval('SEQ_EG_ACTION'),'Common-Reports',true,'common',(select id from eg_module where name='Common' and parentmodule is null),'Reports',3);
insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application,createdby, createddate, lastmodifiedby, lastmodifieddate) values(nextval('SEQ_EG_ACTION'),'CompensationReport','/elastic/compensationreport/',(select id from eg_module where name='Common-Reports' ),1,'Compensation Report',true,'wtms',(select id from eg_module where name='Common' and parentmodule is null),1,now(),1,now());
insert into eg_action (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'ajaxApplicationType','/elastic/compensationreport/ajax-applicationType',null,(select id from eg_module where name='Common-Reports'),null,'ajaxApplicationType','false','wtms',0,1,now(),1,now(),(select id from eg_module where name='Common'));
insert into eg_roleaction values((select id from eg_role where name='SYSTEM'),(select id from eg_action where name='CompensationReport'));
insert into eg_roleaction values((select id from eg_role where name='SYSTEM'),(select id from eg_action where name='ajaxApplicationType'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Compensation Report','Compensation Report',(select id from eg_module where name='Common'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'CompensationReport') ,(select id FROM eg_feature WHERE name = 'Compensation Report'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ajaxApplicationType') ,(select id FROM eg_feature WHERE name = 'Compensation Report'));

insert into eg_appconfig ( ID, KEY_NAME, DESCRIPTION, MODULE,VERSION ) VALUES ( nextval('SEQ_EG_APPCONFIG'), 'COMPENSATION AMOUNT','Compensation amount',(select id from eg_module where name='Common'),0);
insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE,VERSION) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='COMPENSATION AMOUNT'),current_date,0,0);
