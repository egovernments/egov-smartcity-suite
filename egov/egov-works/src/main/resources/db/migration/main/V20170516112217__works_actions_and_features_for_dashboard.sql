--eg_actions for Works Dashboard
insert into eg_action(ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'StateWiseTypeOfWorkDetails','/worksdashboard/statewisetypeofwork',null,(select id from EG_MODULE where name = 'WorksReports'),1,'State Wise Type Of Work Details','false','egworks',0,1,now(),1,now(),(select id from eg_module where name = 'Works Management'));
insert into eg_action(ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'DistrictWiseByTypeOfWork','/worksdashboard/districtwise-bytypeofwork',null,(select id from EG_MODULE where name = 'WorksReports'),1,'District Wise Type Of Work','false','egworks',0,1,now(),1,now(),(select id from eg_module where name = 'Works Management'));
insert into eg_action(ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'UlbWiseByTypeOfWork','/worksdashboard/ulbwise-bytypeofwork',null,(select id from EG_MODULE where name = 'WorksReports'),1,'ULB Wise Type Of Work','false','egworks',0,1,now(),1,now(),(select id from eg_module where name = 'Works Management'));
insert into eg_action(ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'UlbWiseByDistrictAndTypeOfWork','/worksdashboard/ulbwise-bydistrictandtypeofwork',null,(select id from EG_MODULE where name = 'WorksReports'),1,'ULB Wise By District And Type Of Work','false','egworks',0,1,now(),1,now(),(select id from eg_module where name = 'Works Management'));
insert into eg_action(ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'UlbWiseByTypeOfWorkAndUlbs','/worksdashboard/ulbwise-bytypeofworkandulbs',null,(select id from EG_MODULE where name = 'WorksReports'),1,'ULB Wise Type Of Work And ULBS','false','egworks',0,1,now(),1,now(),(select id from eg_module where name = 'Works Management'));
insert into eg_action(ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'StateWiseULBDetails','/worksdashboard/statewiseulb',null,(select id from EG_MODULE where name = 'WorksReports'),1,'State Wise ULB Details','false','egworks',0,1,now(),1,now(),(select id from eg_module where name = 'Works Management'));
insert into eg_action(ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'SectorWiseULBDetails','/worksdashboard/sectorwisereport',null,(select id from EG_MODULE where name = 'WorksReports'),1,'Sector Wise ULB Details','false','egworks',0,1,now(),1,now(),(select id from eg_module where name = 'Works Management'));

--feature and feature actions for Works Dashboard
insert into eg_feature(ID,NAME,DESCRIPTION,MODULE) values (NEXTVAL('seq_eg_feature'),'StateWiseTypeOfWorkDetails','StateWiseTypeOfWorkDetails',(select id from EG_MODULE where name = 'Works Management'));
insert into eg_feature(ID,NAME,DESCRIPTION,MODULE) values (NEXTVAL('seq_eg_feature'),'DistrictWiseByTypeOfWork','DistrictWiseByTypeOfWork',(select id from EG_MODULE where name = 'Works Management'));
insert into eg_feature(ID,NAME,DESCRIPTION,MODULE) values (NEXTVAL('seq_eg_feature'),'UlbWiseByTypeOfWork','UlbWiseByTypeOfWork',(select id from EG_MODULE where name = 'Works Management'));
insert into eg_feature(ID,NAME,DESCRIPTION,MODULE) values (NEXTVAL('seq_eg_feature'),'UlbWiseByDistrictAndTypeOfWork','UlbWiseByDistrictAndTypeOfWork',(select id from EG_MODULE where name = 'Works Management'));
insert into eg_feature(ID,NAME,DESCRIPTION,MODULE) values (NEXTVAL('seq_eg_feature'),'UlbWiseByTypeOfWorkAndUlbs','UlbWiseByTypeOfWorkAndUlbs',(select id from EG_MODULE where name = 'Works Management'));
insert into eg_feature(ID,NAME,DESCRIPTION,MODULE) values (NEXTVAL('seq_eg_feature'),'StateWiseULBDetails','StateWiseULBDetails',(select id from EG_MODULE where name = 'Works Management'));
insert into eg_feature(ID,NAME,DESCRIPTION,MODULE) values (NEXTVAL('seq_eg_feature'),'SectorWiseULBDetails','SectorWiseULBDetails',(select id from EG_MODULE where name = 'Works Management'));

insert into eg_feature_action (ACTION, FEATURE) values ((select id FROM eg_action WHERE name = 'StateWiseTypeOfWorkDetails'),(select id FROM eg_feature WHERE name = 'StateWiseTypeOfWorkDetails'));
insert into eg_feature_action (ACTION, FEATURE) values ((select id FROM eg_action WHERE name = 'DistrictWiseByTypeOfWork'),(select id FROM eg_feature WHERE name = 'DistrictWiseByTypeOfWork'));
insert into eg_feature_action (ACTION, FEATURE) values ((select id FROM eg_action WHERE name = 'UlbWiseByTypeOfWork'),(select id FROM eg_feature WHERE name = 'UlbWiseByTypeOfWork'));
insert into eg_feature_action (ACTION, FEATURE) values ((select id FROM eg_action WHERE name = 'UlbWiseByDistrictAndTypeOfWork'),(select id FROM eg_feature WHERE name = 'UlbWiseByDistrictAndTypeOfWork'));
insert into eg_feature_action (ACTION, FEATURE) values ((select id FROM eg_action WHERE name = 'UlbWiseByTypeOfWorkAndUlbs'),(select id FROM eg_feature WHERE name = 'UlbWiseByTypeOfWorkAndUlbs'));
insert into eg_feature_action (ACTION, FEATURE) values ((select id FROM eg_action WHERE name = 'StateWiseULBDetails'),(select id FROM eg_feature WHERE name = 'StateWiseULBDetails'));
insert into eg_feature_action (ACTION, FEATURE) values ((select id FROM eg_action WHERE name = 'SectorWiseULBDetails'),(select id FROM eg_feature WHERE name = 'SectorWiseULBDetails'));

--rollback delete from eg_feature_action where action in ((select id FROM eg_action WHERE name = 'StateWiseTypeOfWorkDetails'),
--rollback 												(select id FROM eg_action WHERE name = 'DistrictWiseByTypeOfWork'),
--rollback 												(select id FROM eg_action WHERE name = 'UlbWiseByTypeOfWork'),
--rollback 												(select id FROM eg_action WHERE name = 'UlbWiseByDistrictAndTypeOfWork'),
--rollback 												(select id FROM eg_action WHERE name = 'UlbWiseByTypeOfWorkAndUlbs'),
--rollback 												(select id FROM eg_action WHERE name = 'StateWiseULBDetails'),
--rollback 												(select id FROM eg_action WHERE name = 'SectorWiseULBDetails')); 

--rollback delete from eg_feature where name in ('StateWiseTypeOfWorkDetails','DistrictWiseByTypeOfWork','UlbWiseByTypeOfWork','UlbWiseByDistrictAndTypeOfWork','UlbWiseByTypeOfWorkAndUlbs','StateWiseULBDetails','SectorWiseULBDetails') 
--rollback						and module in ((select id from EG_MODULE where name = 'Works Management'));

--rollback delete from eg_action where name in ('StateWiseTypeOfWorkDetails','DistrictWiseByTypeOfWork','UlbWiseByTypeOfWork','UlbWiseByDistrictAndTypeOfWork','UlbWiseByTypeOfWorkAndUlbs','StateWiseULBDetails','SectorWiseULBDetails') and contextroot in ('egworks');
