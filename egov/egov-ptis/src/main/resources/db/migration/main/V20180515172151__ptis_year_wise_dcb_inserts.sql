------------------inserting apconfig value to get the date from which year wise DCB should be enabled
Insert into eg_appconfig (id,key_name,description,version,createdby,lastmodifiedby,createddate,lastmodifieddate,module) 
  values (nextval('SEQ_EG_APPCONFIG'),'PTIS_YEAR_WISE_DCB_DATE','Key to modify the year from which you need the DCB', 0, 
(select id from eg_user where username='egovernments'),(select id from eg_user where username='egovernments'),now(),now(),
(select id from eg_module where name='Property Tax'));

Insert into eg_appconfig_values (id, key_id, effective_from, createdby,lastmodifiedby,createddate,lastmodifieddate,value, version) values 
  (nextval('SEQ_EG_APPCONFIG_VALUES'), (select id from eg_appconfig where key_name = 'PTIS_YEAR_WISE_DCB_DATE' and module = (select id from eg_module where name = 'Property Tax')),
  now(), (select id from eg_user where username='egovernments'),(select id from eg_user where username='egovernments'),now(),now(), '2016-04-01', 0);


-----------Inserting eg_Action
INSERT INTO EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'YearwiseDCBReport','/report/yearwisedcbreport',null,(select id from eg_module where name='PTIS-Reports'),3,'Yearwise DCB Report',true,'ptis',0,1,now(),1,now(),(select id from eg_module where name='Property Tax'));



INSERT INTO EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'YearwiseDCBReportResult','/report/yearwisedcbreport/result',null,(select id from eg_module where name='PTIS-Reports'),1,'Yearwise DCB Report Result',false,'ptis',0,1,now(),1,now(),(select id from eg_module where name='Property Tax'));

----------------------Inserting role action for yearwise DCB report
INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'YearwiseDCBReport'),id from eg_role where name in ('ERP Report Viewer');

INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'YearwiseDCBReportResult'),id from eg_role where name in ('ERP Report Viewer');



